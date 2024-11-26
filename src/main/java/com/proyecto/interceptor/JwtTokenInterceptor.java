package com.proyecto.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class JwtTokenInterceptor implements RequestInterceptor {
    private  HttpSession session;

    /**
     * Constructor de JwtTokenInterceptor.
     *
     * @param session la sesión HTTP actual.
     */
    public JwtTokenInterceptor(HttpSession session) {
        this.session = session;
    }

    /**
     * Metodo que se ejecuta para cada solicitud y añade el token JWT al encabezado de la solicitud.
     *
     * @param template la plantilla de la solicitud.
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
             session = attributes.getRequest().getSession(false);
            if (session != null) {
                String token = (String) session.getAttribute("JWT_TOKEN");
                if (token != null) {
                    template.header("Authorization", "Bearer " + token);
                }
            }
        }
    }
}