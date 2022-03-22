layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        url : '/crm' +'/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });

    /**
     * Listening on the head toolbar
     */
    table.on("toolbar(cusDevPlans)", function (data) {
        if (data.event == "add") {
            // Opens the add or update page
            openAddOrUpdateCusDevPlanDialog();
        } else if (data.event == "success") {
            updateSaleChanceDevResult(2); // Dev Success
        } else if (data.event == "failed") {
            updateSaleChanceDevResult(3); // Dev Failure
        }
    });

    table.on('tool(cusDevPlans)', function (data) {
        if (data.event == 'edit') {
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        } else if (data.event == 'del') {
            deleteCusDevPlan(data.data.id);
        }
    });

    /**
     * Deletes the dev plan item
     * @param id
     */
    function deleteCusDevPlan(id) {
        layer.confirm("Confirms to delete this data record?", {icon: 3, title: 'Dev Plan data management'}, function (index) {
            $.post("/crm" + "/cus_dev_plan/delete", {id: id}, function (result) {
               if (result.code == 200) {
                   layer.msg("Deleting data record success!", {icon: 6});
                   tableIns.reload();
               } else {
                   layer.msg(result.msg, {icon: 5})
               }
            });
        });
    }

    /**
     * Opens the add or update page
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        let title = "Plan item management - add plan item";
        let url = "/crm" + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name=id]").val();

        if (id != null && id != '') {
            title = "Plan item management - update plan item";
            url += "&id=" + id;
        }

        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['500px', '300px'],
            content: url,
            // Able to minimize/maximize
            maxmin: true
        })
    }

    /**
     * Updates the Dev result of sale chance
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult) {
        layer.confirm("Confirms to execute this operation?", {icon: 3, title: "Sale Chance management"}, function (index) {
            let sId = $('[name=id]').val();
            $.post("/crm" + "/sale_chance/updateSaleChanceDevResult", {id: sId, devResult: devResult}, function (result) {
               if (result.code == 200) {
                   layer.msg("Updating success", {icon: 6});
                   layer.closeAll("iframe");
                   parent.location.reload();
               }  else {
                   layer.msg(result.msg, {icon: 5});
               }
            });
        });
    }

});
