layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();

    /**
     * User Logout
     */
    $(".login-out").click(function () {
        // Popping a dialog box which asks the user
        layer.confirm("Confirm to logout", {icon: 3, title: 'Prompt'}, function (index) {
            // Closes the dialog box
            layer.close(index);

            // Removes cookie with domain to be localhost and path to be /crm
            $.removeCookie("userIdEncoded", {domain: "localhost", path: "/crm"});
            $.removeCookie("userName", {domain: "localhost", path: "/crm"});
            $.removeCookie("trueName", {domain: "localhost", path: "/crm"});

            // Go to the logging page
            window.parent.location.href = "/crm/index"
        });
    });
});