package com.frank.crm;

import com.alibaba.fastjson.JSON;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.exceptions.AuthException;
import com.frank.crm.exceptions.NoLoginException;
import com.frank.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Global exception handling.
 */
@Component // Pass this class (GlobalExceptionResolver) to the IOC for management and to be instantiated.
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * This method must be implemented when the class implements HandlerExceptionResolver, and it handles the exception
     * globally. The return values of the methods that handle exception could be either
     *      1. View,
     *      2. or JSON data
     * To determine the return values of the method,
     *      1. If there is @Responsebody annotation on the method (Using reflection), the method it returns JSON data.
     *      2. Otherwise, the method returns a View.
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param e
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {

        /**
         * Determines if a NoLoginException is thrown. If true, then direct the page to login page and require the user
         * to login.
         */
        if (e instanceof NoLoginException) {
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }

        // Sets default ModelAndView code and msg values
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "System error, please try again...");

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // Gets the ResponseBody annotation from the method
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // When the method returns a View
            if (responseBody == null) {

                // If the exception is the ParamsException that we defined, we replace the default code and msg values
                // in ModelAndView to the code and msg stored in the ParamsException. Otherwise, it would still be the
                // default code and msg.
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                } else if (e instanceof AuthException) {
                    AuthException p = (AuthException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }
                return modelAndView;
            } else {
                // Sets default exception code and message
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("System error, please try again...");

                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                } else if (e instanceof AuthException) {
                    AuthException p = (AuthException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                // Sets response type and charset
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter out = null;
                try {
                    out = httpServletResponse.getWriter();
                    String json = JSON.toJSONString(resultInfo);
                    out.write(json);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return modelAndView;
    }
}
