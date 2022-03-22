layui.use(['table','treetable'],function () {
    let $ = layui.jquery;
    let table = layui.table;
    let treeTable = layui.treetable;

    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: '/crm' + '/module/list',
        toolbar: '#toolbarDemo',
        treeDefaultClose: true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if (d.grade == 1) {
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 100, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });

    table.on('tool(munu-table)', function (data) {
        if (data.event == 'add') {
            if (data.data.grade == 2) {
                layer.msg("Adding menu of grade 3 is not available yet", {icon: 5});
                return;
            }
            openAddModuleDialog(data.data.grade + 1, data.data.id);
        } else if (data.event == 'edit') {
            openUpdateModuleDialog(data.data.id);
        } else if (data.event == 'del') {
            deleteModule(data.data.id);
        }
    });

    function deleteModule(id) {
        layer.confirm("Confirms to delete this resource data?", {icon: 3, title: "Resource management - delete resource"}, function (data) {
            $.post("/crm" + "/module/delete", {id: id}, function (result) {
                if (result.code == 200) {
                    layer.msg("Deleting success", {icon: 6});
                    window.location.reload();
                } else {
                    layer.msg(result.msg, {icon: 5});
                }
            });
        });
    }

    function openUpdateModuleDialog(id) {
        let title = "<h3>Resources management - updating resources</h3>"
        let url = "/crm" + "/module/toUpdateModulePage?id=" + id;

        layui.layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["700px", "450px"],
            maxmin: true
        });
    }

    table.on('toolbar(munu-table)', function (data) {
        if (data.event == "expand") {
            treeTable.expandAll("#munu-table");
        } else if (data.event == "fold") {
            treeTable.foldAll("#munu-table");
        } else if (data.event == 'add') {
            openAddModuleDialog(0, -1);
        }
    });

    /**
     * Opens the dialog which adds modules
     * @param grade
     * @param parentId
     */
    function openAddModuleDialog(grade, parentId) {
        let title = "<h3>Resources management - adding resources</h3>"
        let url = "/crm" + "/module/toAddModulePage?grade=" + grade + "&parentId=" + parentId;

        layui.layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["700px", "450px"],
            maxmin: true
        });
    }
});
