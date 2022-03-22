layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    let tableIns = table.render({
        elem: '#roleList',
        url : '/crm' + '/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名称', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    $(".search_btn").click(function () {
        tableIns.reload({
            where: {
                roleName: $("[name='roleName']").val()
            },
            page: {
                curr: 1
            }
        });
    });

    /**
     * Opens the adding/updating role dialog
     */
    function openAddOrUpdateRoleDialog(id) {
        let title = "<h3>Role management - adding role</h3>";
        let url = "/crm" + "/role/toAddOrUpdateRolePage";

        if (id != null && id != '') {
            title = "<h3>Role management - updating role</h3>";
            url += "?id=" + id;
        }

        layui.layer.open({
            title: title,
            content: url,
            area: ["600px", "300px"],
            type: 2,
            maxmin: true
        });
    }

    /**
     * Opens the page to grant resources to a role
     * @param data
     */
    function openAddGrantDialog(data) {
        if (data.length == 0) {
            layer.msg("Please choose the role to grant resources", {icon: 5})
            return;
        }
        if (data.length > 1) {
            layer.msg("Granting a batch of roles is not yet available", {icon: 5});
            return;
        }
        let url = "/crm" + "/module/toAddGrantPage?roleId=" + data[0].id;
        let title = "<h3>Roles management - resources granting</h3>"

        layui.layer.open({
            title: title,
            content: url,
            type: 2,
            area: ["600px", "600px"],
            maxmin: true
        })
    }

    table.on("toolbar(roles)", function (data) {
        if (data.event == 'add') {
            openAddOrUpdateRoleDialog();
        } else if (data.event == 'grant') {
            let checkStatus = table.checkStatus(data.config.id);
            openAddGrantDialog(checkStatus.data);
        }
    })

    table.on("tool(roles)", function (data) {
        if (data.event == 'edit') {
            openAddOrUpdateRoleDialog(data.data.id)
        } else if (data.event == 'del') {
            deleteRole(data.data.id);
        }
    });

    function deleteRole(roleId) {
        layer.confirm("Confirming to delete this data?", {icon: 3, title: "Role Management"}, function (index) {
            // When the user clicks "confirm"
            layer.close(index);
            $.ajax({
                type: "post",
                url: "/crm" + "/role/delete",
                data: {
                    roleId: roleId
                },
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg("Deleting success", {icon: 6});
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            })
        });
    }

});