package com.example.protetion_logiciels.DTO;

import lombok.Data;
import java.time.LocalDate;

public class ContratAssistanceDTO {

    // ── What the frontend sends (POST / PUT) ──────────────────────────────
    @Data
    public static class Request {
        private String numeroContrat;
        private Integer clientId;       // just the ID, not the full object
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private Float montant;
        private String statut = "actif";
    }

    // ── What the frontend receives (GET) ──────────────────────────────────
    @Data
    public static class Response {
        private Integer id;
        private String numeroContrat;
        private Integer clientId;
        private String clientNom;       // flattened — no lazy-proxy issues
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private Float montant;
        private String statut;
    }
}