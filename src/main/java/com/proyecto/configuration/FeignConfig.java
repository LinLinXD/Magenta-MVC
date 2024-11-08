package com.proyecto.configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor authenticationInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpSession session = attributes.getRequest().getSession(false);
                if (session != null && session.getAttribute("JWT_TOKEN") != null) {
                    String token = (String) session.getAttribute("JWT_TOKEN");
                    requestTemplate.header("Authorization", "Bearer " + token);
                }
            }
        };
    }
}