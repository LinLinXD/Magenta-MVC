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
        logger.debug("Verificando rol para {}", joinPoint.getSignature().getName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new SecurityException("Error de seguridad: No hay contexto de request");
        }

        HttpSession session = attributes.getRequest().getSession(false);
        if (session == null) {
            throw new SecurityException("No hay sesión activa");
        }

        @SuppressWarnings("unchecked")
        Set<String> roles = (Set<String>) session.getAttribute("USER_ROLES");
        logger.debug("Roles del usuario: {}", roles);

        if (roles == null || !roles.contains(requireRole.value())) {
            throw new SecurityException("No tienes el rol necesario: " + requireRole.value());
        }

        return joinPoint.proceed();
    }
}