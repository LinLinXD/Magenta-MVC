package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDTO {
    /**
     * Nombre del usuario.
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
     * Nombre de usuario. Útil si el usuario necesita actualizar su nombre de usuario.
     */
    private String username;

    /**
     * Imagen de perfil del usuario. Este campo es necesario para recibir la imagen del usuario.
     */
    private MultipartFile profileImage;
}