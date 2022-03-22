layui.use(['table','layer', 'form'],function() {
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#customerLossList',
        url: '/crm' + '/customer_loss/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerLossListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'cusNo', title: '客户编号', align: "center"},
            {field: 'cusName', title: '客户名称', align: "center"},
            {field: 'cusManager', title: '客户经理', align: "center"},
            {field: 'lastOrderTime', title: '最后下单时间', align: "center"},
            {field: 'lossReason', title: '流失原因', align: "center"},
            {field: 'confirmLossTime', title: '确认流失时间', align: "center"},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'updateDate', title: '更新时间', align: 'center'},
            {title: '操作', minWidth: 150, templet: '#op', fixed: "right", align: "center"}
        ]]
    });

    $(".search_btn").click(function() {
        /**
         * Reloads the table (when doing multi-conditional query). (JQuery)
         */
        tableIns.reload({
            // Sets the parameters that need to be passed to the backend.
            where: {
                // First query parameter
                customerName: $("[name='cusName']").val(),
                // Second query parameter
                customerNo: $("[name='cusNo']").val(),
                // Third query parameter
                state: $("#state").val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    table.on("tool(customerLosses)", function (data) {
        if (data.event == 'add') {
            openCustomerLossDialog("<h3>Loss Management - maintaining the suspending methods</h3>", data.data.id);
        } else if (data.event == 'info') {
            openCustomerLossDialog("<h3>Loss Management - suspending data access</h3>", data.data.id);
        }
    })

    /**
     * Opens the adding suspend or info page
     * @param title
     * @param lossId
     */
    function openCustomerLossDialog(title, lossId) {
        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['700px', '500px'],
            content: "/crm" + "/customer_loss/toCustomerLossPage?lossId=" + lossId,
            // Able to minimize/maximize
            maxmin: true
        });
    }
});