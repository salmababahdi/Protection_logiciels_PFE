package com.example.protetion_logiciels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Integer id;
    private String  codeClient;
    private String  raisonSociale;
    private String  siret;
    private String  email;
    private String  telephone;
    private String  adresse;
    private String  ville;
    private String  statut;
}
