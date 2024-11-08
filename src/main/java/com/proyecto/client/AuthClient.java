package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.AuthDTO;
import com.proyecto.dto.LoginDTO;
import com.proyecto.dto.RegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "localhost:8080", configuration = FeignConfig.class)
public interface AuthClient {
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO register(@RequestBody RegisterDTO registerDTO);

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthDTO login(@RequestBody LoginDTO loginDTO);

    @GetMapping("/dashboard")
    void validateDashboardAccess();
}
