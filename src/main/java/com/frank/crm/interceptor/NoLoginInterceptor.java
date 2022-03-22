package com.frank.crm.interceptor;

import com.frank.crm.dao.UserMapper;
import com.frank.crm.exceptions.NoLoginException;
import com.frank.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The NoLoginInterceptor intercepts the invalid request from the users. For example, the interceptor keeps the user from
 * accessing the "basic information" and "changing password" page. The configuration of the interceptor is implemented in
 * config.MvcConfig
 */
public class NoLoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    /**
     * This method determines if the user is logged in and will be executed before the targeted method. It returns a
     * boolean value: if returning true, it means that the targeted method could be executed; else, it will intercept
     * the execution of the targeted method. To see if the user is logged in, see if the cookie contains user information,
     * and see if the database contains the value of the user id in the cookie. If the user is logged in, then it will
     * allow the targeted method to execute; else it would throw a NoLoginException, which will then be caught by the
     * GlobalExceptionResolver. If it throws a NoLoginException, the page would be directed to the login page.
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId == null || userMapper.selectByPrimaryKey(userId) == null) {
            throw new NoLoginException();
        }
        return true;
    }
}
