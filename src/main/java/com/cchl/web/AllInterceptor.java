package com.cchl.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AllInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        HttpSession session = request.getSession();
        String url = request.getRequestURI();
        logger.info("路径：{}", url);
        if (request.getRequestURI().contains("index")
                || request.getRequestURI().contains("registry")
                || request.getRequestURI().contains("login")
                || request.getRequestURI().contains("fail"))
            return true;
        if (session.getAttribute("id") == null || session.getAttribute("token") == null) {
            response.sendRedirect("/index");
            return false;
        } else {
            if (url.contains("show") || url.contains("msg/select")){
                if ("admin".equals(session.getAttribute("token")))
                    return true;
                else
                    response.sendRedirect("/index");
                return false;
            } else if (url.contains("admin") && !"admin".equals(session.getAttribute("token"))){
                response.sendRedirect("/index");
                return false;
            } else if (url.contains("teacher") && !"teacher".equals(session.getAttribute("token"))) {
                response.sendRedirect("/index");
                return false;
            } else if (url.contains("student") && !"student".equals(session.getAttribute("token"))) {
                response.sendRedirect("/index");
                return false;
            }
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, Exception e) throws Exception {

    }
}
