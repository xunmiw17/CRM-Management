layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addOrUpdateRole)", function (data) {
        let index = layer.msg("The data is being submitted...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        // Sends an AJAX request
        let url = "/crm" + "/role/add";

        let roleId = $("[name='id']").val();
        if (roleId != null && roleId != '') {
            url = "/crm" + "/role/update"
        }
        console.log(data.field);

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

        return false;
    })

    $("#closeBtn").click(function () {
        let index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

});