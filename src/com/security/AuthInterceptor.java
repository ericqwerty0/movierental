package com.security;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger("AuthInterceptor");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        logger.info(" Pre handle ");
        System.out.println("prehandle");
        String currentSessionId = request.getRequestedSessionId();
        String saveSessionId = (String)request.getSession().getAttribute("sessionId");
        
        System.out.println(currentSessionId + " " + saveSessionId);
        if(currentSessionId.equals(saveSessionId) == false) {
            response.sendRedirect(request.getContextPath() + "/login/timeout");
           return false;
        }else
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        logger.info(" Post handle ");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        logger.info(" After Completion ");
    }
    
}
