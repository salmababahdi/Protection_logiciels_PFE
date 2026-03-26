package com.example.protetion_logiciels.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Logiciels")
public class Logiciel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_logiciel", nullable = false, unique = true, length = 50)
    private String codeLogiciel;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 50)
    private String version;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String statut = "actif";
}
