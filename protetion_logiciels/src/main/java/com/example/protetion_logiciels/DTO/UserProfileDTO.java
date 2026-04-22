package com.example.protetion_logiciels.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Integer id;
    private String  name;      // prenom + " " + nom
    private String  email;
    private String  username;
    private String  role;
    private Boolean actif;
}
