layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * Listening to submit event
     */
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        // Loading layer when submitting the data
        let index = layer.msg("The data is being submitted...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        // Sends an AJAX request
        let url = "/crm/sale_chance/add";

        let saleChanceId = $("[name='id']").val();
        if (saleChanceId != null && saleChanceId != '') {
            url = "/crm/sale_chance/update";
        }

        $.post(url, data.field, function (result) {
            // Determines if the operation succeeds
            if (result.code == 200) {
                layer.msg("Operation succeeds", {icon: 6});
                // Closes the loading layer
                layer.close(index);
                // Closes the iframe layer
                layer.closeAll("iframe");
                // Refreshes the parent window and reloads the data table
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });

        // Prevents the form to be submitted
        return false;
    });

    // Closes the dialog
    $("#closeBtn").click(function() {
        let index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    /**
     * Loads the dropdown menu of the Assign Man value
     */
    $.ajax({
       type: "get",
       url: "/crm" + "/user/queryAllSales",
       success: function (data) {
           if (data != null) {
               // Get assign man id from the hidden scope
               let assignManId = $("#assignManId").val();

               for (let i = 0; i < data.length; i++) {
                   let opt = "";
                   if (assignManId == data[i].id) {
                       // Sets the value to be selected
                       opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                   } else {
                       opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                   }
                   $("#assignMan").append(opt);
               }
           }
           // Redraws the content of dropdown menu
           layui.form.render("select");
       }
    });
});