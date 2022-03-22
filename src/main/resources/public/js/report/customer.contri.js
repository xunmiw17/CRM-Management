layui.use(['table','layer', 'form', 'laydate'],function() {
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate = layui.laydate;

        laydate.render({
           elem: '#time'
        });
    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#contriList',
        url: '/crm' + '/customer/queryCustomerContributionByParams',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerContriListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "name", title: '客户名', align: "center", minWidth: 50},
            {field: 'total', title: '总金额（¥）', align: "center", minWidth: 50},
        ]]
    });

    $(".search_btn").click(function () {
        /**
         * Reloads the table (when doing multi-conditional query). (JQuery)
         */
        table.reload("customerContriListTable", {
            // Sets the parameters that need to be passed to the backend.
            where: {
                // First query parameter
                customerName: $("[name='customerName']").val(),
                // Second query parameter
                type: $("#type").val(),
                time: $("[name='time']").val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });
});