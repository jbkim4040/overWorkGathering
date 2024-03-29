package com.overWorkGathering.main.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.logging.Logger;

@Slf4j
public class Interceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        // AuthBean aBean= (AuthBean)session.getAttribute("aBean");

        System.out.println("로그인 세션 확인 :: " + session.getAttribute("login"));


        if (!"000".equals(session.getAttribute("login"))) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>location.href=\"/login\"</script>");
            out.close();
            String referrer = request.getHeader("Referer");
            request.getSession().setAttribute("url_prior_login", referrer);
            response.sendRedirect("/auth/login");
            return false;
        }else{
            return true;
        }
    }
}