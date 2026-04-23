package com.example.protetion_logiciels.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Licences")
public class Licence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cle_licence", nullable = false, unique = true, length = 255)
    private String cleLicence;

    @Column(name = "code_debridage", length = 255)
    private String codeDebridage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logiciel_id", nullable = false)
    private Logiciel logiciel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratAssistance contrat;

    @Column(name = "nb_postes_autorises", nullable = false)
    private Integer nbPostesAutorises = 1;

    @Column(name = "nb_postes_actuels", nullable = false)
    private Integer nbPostesActuels = 0;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false, length = 50)
    private String statut = "actif";
}
