package com.example.protetion_logiciels.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String name;
    private String email;
    private String role;

    public AuthResponse(String token, Integer id, String name, String email, String role) {
        this.token = token;
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.role  = role;
    }
}
