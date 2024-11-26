package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.AuthDTO;
import com.proyecto.dto.LoginDTO;
import com.proyecto.dto.RegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Cliente Feign para el servicio de autenticación.
 */
@FeignClient(
        name = "auth-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface AuthClient {

    /**
     * Registra un nuevo usuario.
     *
     * @param registerDTO el DTO con la información de registro.
     * @return el DTO de autenticación con la información del usuario registrado.
     */
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO register(@RequestBody RegisterDTO registerDTO);

    /**
     * Inicia sesión de un usuario.
     *
     * @param loginDTO el DTO con la información de inicio de sesión.
     * @return el DTO de autenticación con la información del usuario autenticado.
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO login(@RequestBody LoginDTO loginDTO);

    /**
     * Obtiene la información de un usuario.
     *
     * @param username el nombre de usuario del usuario.
     * @return el DTO de autenticación con la información del usuario.
     */
    @GetMapping("/user/info")
    AuthDTO getUserInfo(@RequestParam String username);

    /**
     * Modifica la información de un usuario.
     *
     * @param name el nuevo nombre del usuario.
     * @param email el nuevo correo electrónico del usuario.
     * @param phone el nuevo número de teléfono del usuario.
     * @param username el nombre de usuario del usuario.
     * @param profileImage la nueva imagen de perfil del usuario (opcional).
     * @return el DTO de autenticación con la información del usuario modificado.
     */
    @PostMapping(value = "/modifyUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AuthDTO modifyUser(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("phone") String phone,
            @RequestPart("username") String username,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    );
}