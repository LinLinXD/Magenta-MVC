package com.proyecto.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Set;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> PUBLIC_PATHS = Set.of("/login", "/register", "/css", "/js", "/images");
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getRequestURI();
        logger.debug("Interceptando petición a: {}", path);

        if (isPublicPath(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("JWT_TOKEN") == null) {
            logger.debug("No hay sesión o token JWT");
            response.sendRedirect("/login");
            return false;
        }





        // Ya no verificamos roles aquí, lo dejamos para el aspecto
        return true;
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}