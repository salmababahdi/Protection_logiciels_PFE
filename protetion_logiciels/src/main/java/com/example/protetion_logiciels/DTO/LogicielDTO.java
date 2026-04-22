package com.example.protetion_logiciels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogicielDTO {
    private Integer id;
    private String  codeLogiciel;
    private String  nom;
    private String  version;
    private String  description;
    private String  statut;
}
