package com.proyecto.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para especificar el rol requerido para acceder a un metodo
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    /**
     * El valor del rol requerido.
     *
     * @return Retorna el rol necesario para acceder al metodo.
     */
    String value();
}