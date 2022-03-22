layui.use(['table','layer', 'form'],function() {
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 用户列表展示
     */
    let tableIns = table.render({
        elem: '#customerRepList',
        url: '/crm' + '/customer_rep/list?lossId=' + $("[name='id']").val(),
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerRepListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'measure', title: '暂缓措施', align: "center"},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'updateDate', title: '更新时间', align: 'center'},
            {title: '操作', minWidth: 150, templet: '#customerRepListBar', fixed: "right", align: "center"}
        ]]
    });

    table.on("toolbar(customerReps)", function (data) {
        if (data.event == 'add') {
            openAddOrUpdateCustomerReprDialog();
        } else if (data.event == 'confirm') {
            updateCustomerLossState();
        }
    });

    table.on("tool(customerReps)", function (data) {
        if (data.event == 'edit') {
            openAddOrUpdateCustomerReprDialog(data.data.id);
        } else if (data.event == 'del') {
            deleteCustomerRepr(data.data.id);
        }
    });

    /**
     * Updates the customer loss state
     */
    function updateCustomerLossState() {
        layer.confirm("Confirming to mark this customer to be in loss state?", {icon: 3, title: "Customer Loss Management"}, function (index) {
            // When the user clicks "confirm"
            layer.close(index);

            layer.prompt({title: 'Please type in the loss reason', formType: 2}, function (text, index) {
               layer.close(index);
               $.ajax({
                   type: "post",
                   url: "/crm" + "/customer_loss/updateCustomerLossStateById",
                   data: {
                       id: $("[name='id']").val(),
                       lossReason: text
                   },
                   dataType: "json",
                   success: function (result) {
                       if (result.code == 200) {
                           layer.msg("Confirming loss customer success", {icon: 6});
                           layer.closeAll("iframe");
                           parent.location.reload();
                       }  else {
                           layer.msg(result.msg, {icon: 5});
                       }
                   }
               });
            });
        });
    }

    /**
     * Deletes the reprieve data
     * @param id
     */
    function deleteCustomerRepr(id) {
        layer.confirm("Confirming to delete this data?", {icon: 3, title: "Customer Reprieve Management"}, function (index) {
            // When the user clicks "confirm"
            layer.close(index);
            $.ajax({
                type: "post",
                url: "/crm" + "/customer_rep/delete",
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

    /**
     * Opens the adding or updating data reprieving page
     */
    function openAddOrUpdateCustomerReprDialog(id) {
        let title = "<h3>Repriving Management - adding reprieve</h3>";
        let url = "/crm" + "/customer_rep/toAddOrUpdateCustomerReprPage?lossId=" + $("[name='id']").val();

        if (id != null && id != '') {
            title = "<h3>Repriving Management - updating reprieve</h3>";
            url += "&id=" + id;
        }

        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['550px', '220px'],
            content: url,
            // Able to minimize/maximize
            maxmin: true
        });
    }
});