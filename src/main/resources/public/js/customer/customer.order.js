layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var tableIns = table.render({
        elem: '#customerOrderList',
        url: '/crm' + '/order/list?cusId=' + $("input[name='id']").val(),
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerOrderListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'orderNo', title: '订单编号', align: "center"},
            {field: 'orderDate', title: '下单日期', align: "center"},
            {field: 'address', title: '收货地址', align: "center"},
            {field: 'state', title: '支付状态', align: "center", templet: function (d) {
                if (d.state == 1) {
                    return "已支付";
                } else {
                    return "未支付";
                }
                }},
            {field: 'createDate', title: '创建时间', align: "center"},
            {field: 'updateDate', title: '更新时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#customerOrderListBar"}
        ]]
    });

    table.on("tool(customerOrders)", function (data) {
        if (data.event == 'info') {
            let title = "<h3>Customer Management - Checking order details</h3>"
            let url = "/crm" + "/order/toOrderDetailPage?orderId=" + data.data.id;

            layui.layer.open({
                // Type of dialog
                type: 2, // iframe layer
                title: title,
                // Width & Height
                area: ['700px', '400px'],
                content: url,
                // Able to minimize/maximize
                maxmin: true
            });
        }
    })
});
