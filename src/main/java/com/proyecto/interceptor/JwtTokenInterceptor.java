package com.proyecto.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class JwtTokenInterceptor implements RequestInterceptor {
    private final HttpSession session;

    public JwtTokenInterceptor(HttpSession session) {
        this.session = session;
    }

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                String token = (String) session.getAttribute("JWT_TOKEN");
                if (token != null) {
                    template.header("Authorization", "Bearer " + token);
                }
            }
        }
    }

}