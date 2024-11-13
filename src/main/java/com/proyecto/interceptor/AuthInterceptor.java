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
    // Rutas que siempre serán públicas (recursos estáticos)
    private static final Set<String> STATIC_PATHS = Set.of("/css/", "/js/", "/images/", "/login.css", "/home.css");
    private static final Set<String> AUTH_PATHS = Set.of("/login", "/register");
    private static final Set<String> PUBLIC_PATHS = Set.of("/", "/home");
    private static final Set<String> PROTECTED_PATHS = Set.of("/modifyUser", "/user/info");



    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        logger.debug("Interceptando la solicitud para la ruta: {}", path);

        // Si es un recurso estático, permitir acceso inmediatamente
        if (isStaticResource(path)) {
            logger.debug("Permitiendo acceso a recurso estático: {}", path);
            return true;
        }

        HttpSession session = request.getSession(false);
        boolean isAuthenticated = session != null && session.getAttribute("JWT_TOKEN") != null;

        // Si el usuario está autenticado y trata de acceder a login o register
        if (isAuthenticated && isAuthPath(path)) {
            logger.debug("Usuario autenticado intentando acceder a página pública de autenticación. Redirigiendo a /home");
            response.sendRedirect("/home");
            return false;
        }

        // Si el usuario NO está autenticado y trata de acceder a una ruta protegida
        if (!isAuthenticated && isProtectedPath(path)) {
            logger.debug("Usuario no autenticado intentando acceder a una ruta protegida ({}), redirigiendo a /login", path);
            response.sendRedirect("/login");
            return false;
        }

        // Permitir acceso a rutas públicas
        if (isPublicPath(path)) {
            logger.debug("Permitiendo acceso a ruta pública: {}", path);
            return true;
        }

        logger.debug("Permitiendo acceso a la ruta: {}", path);
        return true;
    }


    private boolean isStaticResource(String path) {
        return STATIC_PATHS.stream().anyMatch(path::startsWith) ||
                path.endsWith(".css") || path.endsWith(".js") ||
                path.endsWith(".png") || path.endsWith(".jpg") ||
                path.endsWith(".jpeg") || path.endsWith(".gif");
    }

    private boolean isAuthPath(String path) {
        return AUTH_PATHS.stream().anyMatch(path::equals);
    }

    private boolean isProtectedPath(String path) {
        return PROTECTED_PATHS.stream().anyMatch(path::equals);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::equals);
    }
}