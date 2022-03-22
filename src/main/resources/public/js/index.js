layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * Form submit listening
     */
    form.on('submit(login)', function(data) {
        // Sends AJAX request and pass on username and password to the backend.
        $.ajax({
            type: "post",
            url: "http://localhost:8080/crm/user/login",
            data: {
                userName: data.field.username,
                userPassword: data.field.password
            },
            // result: the returned result from the backend (ResultInfo)
            success: function (result) {
                console.log(result);
                if (result.code == 200) {
                    layer.msg("Login successful", function () {
                        // Determines if the user chose "remember password"
                        if ($("#rememberMe").prop("checked")) {
                            // Stores user information into cookie and sets the expiration date to be 7 days.
                            $.cookie("userIdEncoded", result.result.userIdEncoded, {expires: 7});
                            $.cookie("userName", result.result.userName, {expires: 7});
                            $.cookie("trueName", result.result.trueName, {expires: 7});
                        } else {
                            // Stores user information into cookie
                            $.cookie("userIdEncoded", result.result.userIdEncoded);
                            $.cookie("userName", result.result.userName);
                            $.cookie("trueName", result.result.trueName);
                        }

                        window.location.href = "http://localhost:8080/crm/main";
                    });
                } else {
                    layer.msg(result.msg, {icon: 5});
                }
            }
        })
        return false;
    });
});