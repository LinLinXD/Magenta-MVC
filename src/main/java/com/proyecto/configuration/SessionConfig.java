package com.proyecto.configuration;

import com.proyecto.filter.SessionCreationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class SessionConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<SessionCreationFilter> sessionFilter() {
        FilterRegistrationBean<SessionCreationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionCreationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);  // Establece la prioridad del filtro
        return registrationBean;
    }
}