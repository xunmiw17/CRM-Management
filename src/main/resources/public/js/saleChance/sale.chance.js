layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 营销机会列表展示
     */
    var tableIns = table.render({
        id: 'saleChanceTable',
        elem: '#saleChanceList', // 表格绑定的ID
        // Corresponds to backend controller
        url : "/crm" + "/sale_chance/list", // 访问数据的地址
        // Minimum width of cell
        cellMinWidth : 95,
        page : true, // 开启分页
        // Height of the container
        height : "full-125",
        // User can choose 10 # / page, 15 # / page, 20 # / page, 25 # / page.
        limits : [10,15,20,25],
        // default # for each page
        limit : 10,
        toolbar: "#toolbarDemo",
        cols : [[
            {type: "checkbox", fixed:"center"},
            // field: Attributes should correspond to the attributes of the returned data (SaleChance)
            {field: "id", title:'编号', sort: true, fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'updateTime', title: '更新时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
            }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
            }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value) {
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
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
                state: $("#state").val()
            },
            page: {
                // Query starts from 1st page
                curr: 1
            }
        });
    });

    /**
     * Listening the head toolbar of the data table
     */
    table.on('toolbar(saleChances)', function (data) {
        if (data.event == "add") {
            openSaleChanceDialog();
        } else if (data.event == "del") {
            deleteSaleChance(data);
        }
    });

    /**
     * Opens the window to add or edit sale chance.
     * If saleChanceId is null, then it's adding operation. Else it's editing operation.
     */
    function openSaleChanceDialog(saleChanceId) {
        // Title of dialog
        let title = "<h2>Sale chance management - add sale chance</h2>";
        let url = "/crm/sale_chance/toSaleChancePage"
        if (saleChanceId != null && saleChanceId != '') {
            title = "<h2>Sale chance management - update sale chance</h2>";
            url += '?saleChanceId=' + saleChanceId;
        }

        layui.layer.open({
            // Type of dialog
            type: 2, // iframe layer
            title: title,
            // Width & Height
            area: ['500px', '620px'],
            content: url,
            // Able to minimize/maximize
            maxmin: true
        });
    }

    /**
     * This method deletes multiple sale chances
     * @param data
     */
    function deleteSaleChance(data) {
        // Gets the sale chances data which are selected by the users
        var checkStatus = table.checkStatus("saleChanceTable");
        var saleChanceData = checkStatus.data;
        if (saleChanceData.length < 1) {
            layer.msg("Please select the data to be deleted", {icon: 5});
            return;
        }
        layer.confirm("Confirms to delete the sale chances selected?", {icon: 3, title: "Sale Chance Management"}, function (index) {
            layer.close(index);
            var ids = "ids=";
            for (var i = 0; i < saleChanceData.length - 1; i++) {
                ids += saleChanceData[i].id + "&ids=";
            }
            ids += saleChanceData[saleChanceData.length - 1].id;
            console.log(ids);

            $.ajax({
               type: "post",
               url: "/crm" + "/sale_chance/delete",
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
     * Listening on the table line tool.
     */
    table.on('tool(saleChances)', function (data) {
        if (data.event == 'edit') {
            let saleChanceId = data.data.id;
            openSaleChanceDialog(saleChanceId);
        } else if (data.event == 'del') {
            // Asks if the user really wants to delete the data
            layer.confirm("Confirming to delete this data?", {icon: 3, title: "Sale Chance Management"}, function (index) {
                // When the user clicks "confirm"
                layer.close(index);
                $.ajax({
                    type: "post",
                    url: "/crm" + "/sale_chance/delete",
                    data: {
                        ids: data.data.id
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
});
