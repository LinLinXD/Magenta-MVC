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
    /**
     * Conjunto de rutas estáticas que no requieren autenticación.
     */
    private static final Set<String> STATIC_PATHS = Set.of(
            "/css/", "/js/", "/images/", "/login.css", "/home.css"
    );

    /**
     * Conjunto de rutas de autenticación.
     */
    private static final Set<String> AUTH_PATHS = Set.of("/login", "/register");

    /**
     * Conjunto de rutas públicas.
     */
    private static final Set<String> PUBLIC_PATHS = Set.of("/", "/home");

    /**
     * Logger para registrar eventos.
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    /**
     * Metodo que se ejecuta antes de que el controlador maneje la solicitud.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @param handler  el manejador de la solicitud.
     * @return true si la solicitud debe continuar, false en caso contrario.
     * @throws Exception si ocurre un error durante el manejo de la solicitud.
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getRequestURI();
        logger.debug("Interceptando solicitud para: {}", path);

        if (isStaticResource(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        boolean isAuthenticated = session != null && session.getAttribute("JWT_TOKEN") != null;

        // Si el usuario está autenticado y trata de acceder a login/register
        if (isAuthenticated && isAuthPath(path)) {
            response.sendRedirect("/home");
            return false;
        }

        // Si el usuario NO está autenticado y trata de acceder a rutas protegidas
        if (!isAuthenticated && needsAuthentication(path)) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    /**
     * Verifica si la ruta es un recurso estático.
     *
     * @param path la ruta a verificar.
     * @return true si la ruta es un recurso estático, false en caso contrario.
     */
    private boolean isStaticResource(String path) {
        return STATIC_PATHS.stream().anyMatch(path::startsWith) ||
                path.matches(".*\\.(css|js|png|jpg|jpeg|gif)$");
    }

    /**
     * Verifica si la ruta es una ruta de autenticación.
     *
     * @param path la ruta a verificar.
     * @return true si la ruta es de autenticación, false en caso contrario.
     */
    private boolean isAuthPath(String path) {
        return AUTH_PATHS.contains(path);
    }

    /**
     * Verifica si la ruta es una ruta pública.
     *
     * @param path la ruta a verificar.
     * @return true si la ruta es pública, false en caso contrario.
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.contains(path);
    }

    /**
     * Verifica si la ruta requiere autenticación.
     *
     * @param path la ruta a verificar.
     * @return true si la ruta requiere autenticación, false en caso contrario.
     */
    private boolean needsAuthentication(String path) {
        return !isPublicPath(path) && !isAuthPath(path) && !isStaticResource(path);
    }
}