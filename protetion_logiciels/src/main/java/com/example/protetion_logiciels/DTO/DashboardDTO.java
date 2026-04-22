package com.example.protetion_logiciels.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    // ── KPI cards ─────────────────────────────────────────────
    private long totalClients;
    private long licencesActives;
    private long licencesExpirees;
    private long contratsEnCours;
    private long expirantBientot;      // licences expiring in next 30 days
    private long paiementsAttente;     // contrats with statut = 'en attente'

    // ── Pie chart ─────────────────────────────────────────────
    private long licencesSuspendues;

    // ── Area chart ────────────────────────────────────────────
    private List<EvolutionPoint> evolution;

    // ── Recent alerts ─────────────────────────────────────────
    private List<AlertItem> alerts;

    // ── Nested: one point on the area chart ───────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvolutionPoint {
        private String mois;
        private long   activations;
    }

    // ── Nested: one alert row ─────────────────────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlertItem {
        private String type;   // "danger" | "warning" | "info" | "success"
        private String text;
    }
}
