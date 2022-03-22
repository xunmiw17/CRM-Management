layui.use(['table','layer', 'form'],function() {
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#customerList',
        url: '/crm' + '/customer/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'name', title: '客户名', align: "center"},
            {field: 'fr', title: '法人', align: "center"},
            {field: 'khno', title: '客户编号', align: "center"},
            {field: 'area', title: '地区', align: "center"},
            {field: 'cusManager', title: '客户经理', align: "center"},
            {field: 'myd', title: '满意度', align: "center"},
            {field: 'level', title: '客户级别', align: "center"},
            {field: 'xyd', title: '信用度', align: "center"},
            {field: 'address', title: '详细地址', align: "center"},
            {field: 'postCode', title: '邮编', align: "center"},
            {field: 'phone', title: '电话', align: "center"},
            {field: 'webSite', title: '网站', align: 'center'},
            {field: 'fax', title: '传真', align: 'center'},
            {field: 'zczj', title: '注册资金', align: 'center'},
            {field: 'yyzzzch', title: '营业执照', align: 'center'},
            {field: 'khyh', title: '开户行', align: 'center'},
            {field: 'khzh', title: '开户账号', align: 'center'},
            {field: 'gsdjh', title: '国税', align: 'center'},
            {field: 'dsdjh', title: '地税', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'updateDate', title: '更新时间', align: 'center'},
            {title: '操作', minWidth: 150, templet: '#customerListBar', fixed: "right", align: "center"}
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
                customerName: $("[name='name']").val(),
                // Second query parameter
                customerNo: $("[name='khno']").val(),
                // Third query parameter
                level: $("[name='level']").val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    function openCustomerOrderDialog(data) {
        if (data.length == 0) {
            layer.msg("Please choose the customer to check the order", {icon : 5});
            return;
        }
        if (data.length > 1) {
            layer.msg("Batch checking order is currently not available", {icon: 5});
            return;
        }
        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: "<h3>Customer management - checking order</h3>",
            // Width & Height
            area: ['700px', '500px'],
            content: "/crm" + "/customer/toCustomerOrderPage?id=" + data[0].id,
            // Able to minimize/maximize
            maxmin: true
        });
    }

    /**
     * Listening on the head toolbar
     */
    table.on("toolbar(customers)", function (data) {
        if (data.event == 'add') {
            openAddOrUpdateCustomerDialog();
        } else if (data.event == 'order') {
            let checkStatus = table.checkStatus(data.config.id);
            openCustomerOrderDialog(checkStatus.data);
        }
    });

    function openAddOrUpdateCustomerDialog(id) {
        let title = "<h3>Customer Management - add user</h3>";
        let url = "/crm" + "/customer/toAddOrUpdateCustomerPage";

        if (id != null && id != '') {
            title = "<h3>Customer Management - updating user</h3>";
            url = "/crm" + "/customer/toAddOrUpdateCustomerPage?id=" + id;
        }

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

    table.on("tool(customers)", function (data) {
        if (data.event == 'edit') {
            openAddOrUpdateCustomerDialog(data.data.id);
        } else if (data.event == 'del') {
            deleteCustomer(data.data.id);
        }
    });

    function deleteCustomer(id) {
        layer.confirm("Confirming to delete this data?", {icon: 3, title: "Customer Management"}, function (index) {
            // When the user clicks "confirm"
            layer.close(index);
            $.ajax({
                type: "post",
                url: "/crm" + "/customer/delete?",
                data: {
                    id: id
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