layui.use(['table','layer'], function(){
    let layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 客户开发数据列表
     */
    let tableIns = table.render({
        elem: '#saleChanceList',
        url: '/crm' + '/sale_chance/list?flag=1',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
            }},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
        ]]
    });

    /**
     * 格式化开发状态
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    // Registers click event for the "search" button
    $(".search_btn").click(function() {
        /**
         * Reloads the table (when doing multi-conditional query). (JQuery)
         */
        tableIns.reload({
            // Sets the parameters that need to be passed to the backend.
            where: {
                // First query parameter
                customerName: $("[name='customerName']").val(),
                // Second query parameter
                createMan: $("[name='createMan']").val(),
                // Third query parameter
                devResult: $("#devResult").val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    /**
     * Listening on the line toolbar
     */
    table.on('tool(saleChances)', function (data) {
        if (data.event == "dev") {
            openCusDevPlanDialog('Dev Plan', data.data.id);
        } else if (data.event == "info") {
            openCusDevPlanDialog('Dev maintenance', data.data.id);
        }
    })

    /**
     * Opens the dev plan development or info subpage
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title, id) {

        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['750px', '550px'],
            content: '/crm' + "/cus_dev_plan/toCusDevPlanPage?id=" + id,
            // Able to minimize/maximize
            maxmin: true
        })
    }


});
