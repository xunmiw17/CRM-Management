layui.use(['form', 'layer'], function () {
    let form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(updateModule)", function (data) {
        let index = top.layer.msg("The data is being submitted...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        $.post("/crm" + "/module/update", data.field, function (result) {
            // Determines if the operation succeeds
            if (result.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("Operation succeeds!");
                    layer.closeAll("iframe");
                    parent.location.reload();
                });
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
});