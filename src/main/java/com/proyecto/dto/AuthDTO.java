package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    /**
     * Token de autenticación del usuario.
     */
    private String token;

    /**
     * Roles del usuario.
     */
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Número de teléfono del usuario.
     */
    private String phone;

    /**
     * Mensaje de error, si existe.
     */
    private String error;

    /**
     * URL de la imagen de perfil del usuario.
     */
    private String profileImageUrl;
}