package com.proyecto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    /**
     * Nombre de usuario.
     * Debe ser obligatorio.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    /**
     * Contraseña del usuario.
     * Debe ser obligatoria.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Nombre del usuario.
     * Debe ser obligatorio.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    /**
     * Teléfono del usuario.
     * Debe tener entre 9 y 15 dígitos.
     */
    @Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe tener entre 9 y 15 dígitos")
    private String phone;

    /**
     * Email del usuario.
     * Debe ser un email válido y obligatorio.
     */
    @Email(message = "Debe proporcionar un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
}