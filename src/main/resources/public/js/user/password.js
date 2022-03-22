layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)", function (data) {
       console.log(data.field);

       $.ajax({
           type: "post",
           url: "/crm/user/updatePassword",
           data: {
              oldPassword: data.field.old_password,
              newPassword: data.field.new_password,
              confirmedPassword: data.field.again_password
           },
           success: function (result) {
               if (result.code == 200) {
                   // After changing password successfully, the cookie information was removed within the given host and path.
                   layer.msg("User password changed successfully. The system would log out after 3 sec...", function () {
                       $.removeCookie("userIdEncoded", {domain: "localhost", path: "/crm"});
                       $.removeCookie("userName", {domain: "localhost", path: "/crm"});
                       $.removeCookie("trueName", {domain: "localhost", path: "/crm"});

                       // Changes the path back to home page. (Should be window.parent since otherwise the page would
                       // still be inside the main page.
                       window.parent.location.href = "/crm/index";
                   });
               } else {
                   layer.msg(result.msg, {icon: 5});
               }
           }
       });
    });
});