layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 添加或更新计划项
     */
    form.on("submit(addOrUpdateCusDevPlan)", function (data) {
        let index = top.layer.msg("Data are being submitted, please wait...", {
            icon: 16,
            time: false,
            shade: 0.8
        });
        let formData = data.field;
        let url= "/crm" + "/cus_dev_plan/add";

        if ($('[name="id"]').val()) {
            url = "/crm" + "/cus_dev_plan/update";
        }

        $.post(url, formData, function (result) {
            if (result.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("Operation success ", {icon: 6});
                    // 关闭所有ifream层
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });
        return false;
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

});