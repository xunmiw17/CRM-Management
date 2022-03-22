layui.use(['table','layer'],function(){
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#userList',
        url : '/crm' + '/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    // Registers click event for the "search" button
    $(".search_btn").click(function() {
        /**
         * Reloads the table (when doing multi-conditional query). (JQuery)
         */
        tableIns.reload({
            // Sets the parameters that need to be passed to the backend.
            where: {
                // First query parameter
                userName: $("[name='userName']").val(),
                // Second query parameter
                email: $("[name='email']").val(),
                // Third query parameter
                phone: $('[name=phone]').val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    /**
     * Deletes multiple user data
     * @param userData
     */
    function deleteUsers(userData) {
        if (userData.length == 0) {
            layer.msg("Please select the user data to be deleted", {icon: 5});
            return;
        }
        layer.confirm("Confirms to delete the user data selected?", {icon: 3, title: "User Management"}, function (index) {
            layer.close(index);
            let ids = "ids=";
            for (let i = 0; i < userData.length - 1; i++) {
                ids += userData[i].id + "&ids=";
            }
            ids += userData[userData.length - 1].id;

            $.ajax({
                type: "post",
                url: "/crm" + "/user/delete",
                data: ids,
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg("Deleting success", {icon: 6});
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * Listening on the head toolbar
     */
    table.on("toolbar(users)", function (data) {
        if (data.event == 'add') {
            openAddOrUpdateUserDialog();
        } else if (data.event == 'del') {
            let checkStatus = table.checkStatus(data.config.id);
            deleteUsers(checkStatus.data);
        }
    });

    function openAddOrUpdateUserDialog(id) {
        let title = "<h3>User Management - add user</h3>"
        let url = "/crm" + "/user/toAddOrUpdateUserPage"

        if (id != null && id != '') {
            title = "<h3>User Management - update user</h3>"
            url += "?id=" + id;
        }
        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['650px', '400px'],
            content: url,
            // Able to minimize/maximize
            maxmin: true
        });
    }

    /**
     * Deletes one user record
     * @param id
     */
    function deleteUser(id) {
        layer.confirm("Confirming to delete this data?", {icon: 3, title: "User Management"}, function (index) {
            // When the user clicks "confirm"
            layer.close(index);
            $.ajax({
                type: "post",
                url: "/crm" + "/user/delete",
                data: {
                    ids: id
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

    table.on("tool(users)", function (data) {
        if (data.event == 'edit') {
            openAddOrUpdateUserDialog(data.data.id)
        } else if (data.event == 'del') {
            deleteUser(data.data.id)
        }
    });

});