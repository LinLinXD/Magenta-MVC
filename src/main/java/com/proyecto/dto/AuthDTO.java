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
    private String token;

    @Builder.Default
    private Set<String> roles = new HashSet<>();

    private String username;
    private String name;
    private String email;
    private String phone;
    private String error;

    // Para la imagen en base64
    private String profileImageUrl;
}