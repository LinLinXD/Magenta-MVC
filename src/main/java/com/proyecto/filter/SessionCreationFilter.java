package com.proyecto.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class SessionCreationFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login",
            "/register",
            "/css/",
            "/js/",
            "/images/"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // Si es una ruta pública y ya existe una sesión sin token, la invalidamos
        if (isPublicPath(path)) {
            HttpSession existingSession = httpRequest.getSession(false);
            if (existingSession != null && existingSession.getAttribute("JWT_TOKEN") == null) {
                existingSession.invalidate();
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No es necesario implementar
    }

    @Override
    public void destroy() {
        // No es necesario implementar
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}