package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.AuthDTO;
import com.proyecto.dto.LoginDTO;
import com.proyecto.dto.ModifyUserDTO;
import com.proyecto.dto.RegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import feign.Headers;
import feign.form.spring.SpringFormEncoder;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface AuthClient {

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO register(@RequestBody RegisterDTO registerDTO);

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO login(@RequestBody LoginDTO loginDTO);

    @GetMapping("/user/info")
    AuthDTO getUserInfo(@RequestParam String username);

    @PostMapping(value = "/modifyUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AuthDTO modifyUser(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("phone") String phone,
            @RequestPart("username") String username,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    );
}