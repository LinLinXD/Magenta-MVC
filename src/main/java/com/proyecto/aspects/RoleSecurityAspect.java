package com.proyecto.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Set;


@Aspect
@Component
public class RoleSecurityAspect {

    /**
     * Este aspecto, en su definición, se encarga de interceptar los métodos anotados con @RequireRole
     * y verificar si el usuario tiene el rol requerido para ejecutar el metodo.
     * En dado caso de que el usuario no lo tenga, tirara una excepción de seguridad.
     * usado para el rol de administrador.
     * @param joinPoint Representa el punto de ejecución del metodo anotado con @RequireRole
     * @param requireRole Contiene el rol requerido para ejecutar el metodo
     * @return devuelve el resultado de la ejecución del metodo.
     * @throws Throwable Recoje las excepciones que se puedan generar en la ejecución del metodo.
     */
    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {

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

        if (roles == null || !roles.contains(requireRole.value())) {
            throw new SecurityException("No tienes el rol necesario: " + requireRole.value());
        }

        return joinPoint.proceed();
    }
}