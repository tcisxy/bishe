package com.example.demo.component;

import com.example.demo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    public final static String SESSION_KEY = "user";
    public final static String SESSION_AUTH = "auth";
    public final static String SESSION_INDEX = "index";

    @Autowired
    private AuthComponent authComponent;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        addInterceptor.excludePathPatterns("/css/*");
        addInterceptor.excludePathPatterns("/fonts/*");
        addInterceptor.excludePathPatterns("/images/*");
        addInterceptor.excludePathPatterns("/js/*");
        addInterceptor.excludePathPatterns("/maps/*");
        addInterceptor.excludePathPatterns("/err");
        addInterceptor.excludePathPatterns("/login**");
        addInterceptor.excludePathPatterns("/toLogin**");
        addInterceptor.excludePathPatterns("/signup**");
        addInterceptor.excludePathPatterns("/toSignup**");
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter{
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws IOException {
            HttpSession session = request.getSession();
            if(session.getAttribute(SESSION_KEY) != null) {
                String url = "/";
                String preUrl = request.getRequestURL().toString();
                Employee employee = (Employee) session.getAttribute(SESSION_KEY);
                if(employee.getDeptId() != 1 && url.matches(".*elete.*"))
                    return false;
                List<Integer> indexs = (ArrayList) session.getAttribute(SESSION_INDEX);
                if(!authControl(indexs,preUrl)) {
                    response.sendRedirect(url);
                    return false;
                }
                return true;
            }
            String url = "/login";
            response.sendRedirect(url);
            return false;
        }

        private boolean authControl(List<Integer> indexs,String url) {
            if(!(indexs.contains(0)) && url.matches(".*user.*"))
                return false;
            if(!(indexs.contains(1)) && url.matches(".*doctor.*"))
                return false;
            if(!(indexs.contains(2)) && url.matches(".*appoint.*"))
                return false;
            if(!(indexs.contains(3)) && url.matches(".*vip.*"))
                return false;
            if(!(indexs.contains(4)) && url.matches(".*emp.*"))
                return false;
            if(!(indexs.contains(5)) && url.matches(".*dept.*"))
                return false;
            if(!(indexs.contains(6)) && (url.matches(".*consume.*") || url.matches(".*recharge.*")))
                return false;
            if(!(indexs.contains(7)) && url.matches(".*visit.*"))
                return false;
            return true;
        }
    }

}
