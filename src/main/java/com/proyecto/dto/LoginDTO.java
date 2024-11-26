package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Contraseña del usuario.
     */
    private String password;
}