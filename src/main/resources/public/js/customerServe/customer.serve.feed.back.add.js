layui.use(['table','layer', 'form'],function() {
    let form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * Loads the dropdown menu of the Assign Man value
     */
    $.ajax({
        type: "get",
        url: "/crm" + "/user/queryAllCustomerManagers",
        success: function (data) {
            if (data != null) {
                let assigner = $("[name='man']").val();
                for (let i = 0; i < data.length; i++) {
                    let opt = "";
                    if (data[i].id == assigner) {
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    } else {
                        opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }
                    $("#assigner").append(opt);
                }
            }
            // Redraws the content of dropdown menu
            layui.form.render("select");
        }
    });

    form.on("submit(addOrUpdateCustomerServe)", function (data) {
        let index = top.layer.msg("Data are being submitted, please wait...", {
            icon: 16,
            time: false,
            shade: 0.8
        });

        let formData = data.field;
        let url= "/crm" + "/customer_serve/update";

        $.post(url, formData, function (result) {
            if (result.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("Adding serve success ", {icon: 6});
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