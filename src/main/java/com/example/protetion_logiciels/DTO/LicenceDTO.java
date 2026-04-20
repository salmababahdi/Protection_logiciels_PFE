package com.example.protetion_logiciels.DTO;

import lombok.Data;
import java.time.LocalDate;

public class LicenceDTO {

    // ── What the frontend sends (POST / PUT) ──────────────────────────────
    @Data
    public static class Request {
        private Integer clientId;
        private Integer logicielId;
        private Integer contratId;
        private String cleLicence;
        private String codeDebridage;
        private Integer nbPostesAutorises = 1;
        private Integer nbPostesActuels = 0;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String statut = "actif";
    }

    // ── What the frontend receives (GET) ──────────────────────────────────
    @Data
    public static class Response {
        private Integer id;
        private String cleLicence;
        private String codeDebridage;
        private Integer clientId;
        private String clientNom;
        private Integer logicielId;
        private String logicielNom;
        private Integer contratId;
        private Integer nbPostesAutorises;
        private Integer nbPostesActuels;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String statut;
    }
}
