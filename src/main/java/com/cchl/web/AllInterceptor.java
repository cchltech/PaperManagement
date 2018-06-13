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
        String urls = request.getRequestURI();
        String url = urls.split("/")[1];
        logger.info("路径：{}", urls);
        if (url.contains("index")
                || url.contains("registry")
                || url.contains("login")
                || url.contains("fail")
                || url.contains("majorList")
                || url.contains("studentRegister")
                || url.contains("teacherRegister"))
            return true;
        if (session.getAttribute("id") == null || session.getAttribute("token") == null) {
            response.sendRedirect("/index");
            return false;
        } else {
            if ("admin".contains(url) && !"admin".equals(session.getAttribute("token"))) {
                return false;
            } else if ("student".contains(url) && !"student".equals(session.getAttribute("token"))) {
                return false;
            } else if ("teacher".contains(url) && !"teacher".equals(session.getAttribute("token"))) {
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
