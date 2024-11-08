package com.proyecto.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenInterceptor implements RequestInterceptor {
    private final HttpSession session;

    public JwtTokenInterceptor(HttpSession session) {
        this.session = session;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}