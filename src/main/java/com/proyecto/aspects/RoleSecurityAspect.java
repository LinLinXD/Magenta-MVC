package com.proyecto.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class RoleSecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(RoleSecurityAspect.class);

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        logger.debug("RoleSecurityAspect ejecutándose para {}", joinPoint.getSignature().getName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            logger.error("No se encontraron atributos de request");
            throw new SecurityException("Error de seguridad: No hay contexto de request");
        }

        HttpSession session = attributes.getRequest().getSession(false);
        if (session == null) {
            logger.error("No hay sesión activa");
            throw new SecurityException("No hay sesión activa");
        }

        @SuppressWarnings("unchecked")
        Set<String> roles = (Set<String>) session.getAttribute("USER_ROLES");

        logger.debug("Verificando rol requerido: {} contra roles del usuario: {}",
                requireRole.value(), roles);

        if (roles == null || !roles.contains(requireRole.value())) {
            logger.error("Acceso denegado - rol requerido: {}, roles del usuario: {}",
                    requireRole.value(), roles);
            throw new SecurityException("No tienes el rol necesario: " + requireRole.value());
        }

        logger.debug("Acceso permitido para el rol: {}", requireRole.value());
        return joinPoint.proceed();
    }
}