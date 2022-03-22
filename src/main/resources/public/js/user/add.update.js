layui.use(['form', 'layer', 'formSelects'], function () {
    let form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    let formSelects = layui.formSelects;

    form.on("submit(addOrUpdateUser)", function (data) {
        let index = top.layer.msg("The data is being submitted...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        let url = "/crm/user/add";

        let userId = $("[name='id']").val();
        if (userId != null && userId != '') {
            url = "/crm" + "/user/update";
        }

        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                top.layer.msg("Operation succeeds", {icon: 6});
                // Closes the loading layer
                top.layer.close(index);
                // Closes the iframe layer
                layer.closeAll("iframe");
                // Refreshes the parent window and reloads the data table
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });

        return false;
    });

    $("#closeBtn").click(function() {
        let index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    /**
     * Loads the role select form
     */
    formSelects.config("selectId", {
        type: "post",
        searchUrl: "/crm" + "/role/queryAllRoles?userId=" + $('[name="id"]').val(),
        keyName: "roleName",
        keyVal: "id"
    }, true);
    
});