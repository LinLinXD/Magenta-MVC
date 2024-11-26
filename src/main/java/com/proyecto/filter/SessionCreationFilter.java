package com.proyecto.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SessionCreationFilter implements Filter {

    /**
     * Lista de rutas públicas que no requieren la creación de sesión.
     */
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login",
            "/register",
            "/css/",
            "/js/",
            "/images/"
    );

    /**
     * Metodo principal del filtro que se ejecuta en cada solicitud.
     *
     * @param request  la solicitud entrante.
     * @param response la respuesta saliente.
     * @param chain    la cadena de filtros.
     * @throws IOException      si ocurre un error de entrada/salida.
     * @throws ServletException si ocurre un error en el servlet.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Metodo de inicialización del filtro.
     *
     * @param filterConfig la configuración del filtro.
     * @throws ServletException si ocurre un error en el servlet.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No es necesario implementar
    }

    /**
     * Metodo de destrucción del filtro.
     */
    @Override
    public void destroy() {
        // No es necesario implementar
    }

    /**
     * Verifica si la ruta es una ruta pública.
     *
     * @param path la ruta a verificar.
     * @return true si la ruta es pública, false en caso contrario.
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}