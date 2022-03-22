package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.service.PermissionService;
import com.frank.crm.service.UserService;
import com.frank.crm.utils.UserIDBase64;
import com.frank.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "index"; // Returning "index.ftl" using ViewResolver
    }
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome"; // Returning "welcome.ftl"
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request, @CookieValue("userIdEncoded") String id) {
        // Get the value of user id from cookie
        Integer userId;
        if (StringUtils.isBlank(id)) {
            userId = 0;
        }
        userId = UserIDBase64.decoderUserID(id);
        // Search the user object from database
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user", user);

        // Query the resources the user has according to the current user id (Query the corresponding ACL code to the resources)
        List<String> permissions = permissionService.queryPermissionsByUserId(userId);
        request.getSession().setAttribute("permissions", permissions);

        return "main"; // Returning "main.ftl"
    }
}
