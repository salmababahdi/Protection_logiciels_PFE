package com.example.protetion_logiciels.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_client", nullable = false, unique = true, length = 50)
    private String codeClient;

    @Column(name = "raison_sociale", nullable = false, length = 200)
    private String raisonSociale;

    @Column(length = 20)
    private String siret;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Column(columnDefinition = "TEXT")
    private String adresse;

    @Column(length = 100)
    private String ville;

    @Column(nullable = false, length = 50)
    private String statut = "actif";
}
