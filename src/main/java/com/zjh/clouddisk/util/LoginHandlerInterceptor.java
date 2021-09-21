package com.zjh.clouddisk.util;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 *
 * @author TheZJH
 * @version 1.0
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            //未登录
            request.getSession().setAttribute("msg", "请先登录");
            response.sendRedirect("/login");
            return false;
        } else {
            //已登录
            return true;
        }
    }
}
