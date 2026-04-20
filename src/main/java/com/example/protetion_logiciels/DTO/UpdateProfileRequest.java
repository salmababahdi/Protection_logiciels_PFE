package com.example.protetion_logiciels.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank
    private String name;       // "prenom nom" combined

    @NotBlank
    @Email
    private String email;

    private String telephone;  // stored in a future field; ignored for now
}
