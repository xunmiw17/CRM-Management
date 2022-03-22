layui.use(['table','layer', 'form'],function() {
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#customerServeList',
        url: '/crm' + '/customer_serve/list?state=fw_003',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerServeListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'customer', title: '客户名', align: "center", minWidth: 50},
            {field: 'dicValue', title: '服务类型', align: "center", minWidth: 100},
            {field: 'overview', title: '概要信息', align: "center"},
            {field: 'assignTime', title: '分配时间', align: "center", minWidth: 50},
            {field: 'assigner', title: '分配人', align: "center", minWidth: 100},
            {field: 'createPeople', title: '创建人', align: "center", minWidth: 100},
            {field: 'serviceProcePeople', title: '处理人', align: "center", minWidth: 50},
            {field: 'serviceProceTime', title: '处理时间', align: "center", minWidth: 100},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', templet: '#customerServeListBar', align: 'center', minWidth: 150, fixed: 'right'}
        ]]
    });

    $(".search_btn").click(function () {
        /**
         * Reloads the table (when doing multi-conditional query). (JQuery)
         */
        table.reload("customerServeListTable", {
            // Sets the parameters that need to be passed to the backend.
            where: {
                // First query parameter
                customer: $("[name='customer']").val(),
                // Second query parameter
                serveType: $("#type").val(),
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    table.on("tool(customerServes)", function (data) {
        if (data.event == 'back') {
            openCustomerServeFeedBackDialog(data.data.id);
        }
    });

    function openCustomerServeFeedBackDialog(id) {
        let title = "<h3>Serve Management - Providing feedback</h3>";
        let url = "/crm" + "/customer_serve/toCustomerServeFeedBackPage?id=" + id;

        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['700px', '500px'],
            content: url,
            // Able to minimize/maximize
            maxmin: true
        });
    }
});