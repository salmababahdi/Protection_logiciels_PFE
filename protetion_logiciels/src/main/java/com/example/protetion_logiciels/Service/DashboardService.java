package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.DashboardDTO;
import com.example.protetion_logiciels.Entity.ContratAssistance;
import com.example.protetion_logiciels.Entity.Licence;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.ContratAssistanceRepository;
import com.example.protetion_logiciels.Repository.LicenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClientRepository            clientRepository;
    private final LicenceRepository           licenceRepository;
    private final ContratAssistanceRepository contratRepository;

    public DashboardDTO getStats() {

        List<Licence>           allLicences = licenceRepository.findAll();
        List<ContratAssistance> allContrats = contratRepository.findAll();
        LocalDate               today       = LocalDate.now();
        LocalDate               in30Days    = today.plusDays(30);

        // ── KPI ───────────────────────────────────────────────
        long totalClients     = clientRepository.count();
        long licencesActives  = allLicences.stream().filter(l -> "actif".equalsIgnoreCase(l.getStatut())).count();
        long licencesExpirees = allLicences.stream().filter(l -> "expiré".equalsIgnoreCase(l.getStatut())
                || (l.getDateFin() != null && l.getDateFin().isBefore(today))).count();
        long contratsEnCours  = allContrats.stream().filter(c -> "actif".equalsIgnoreCase(c.getStatut())).count();
        long expirantBientot  = allLicences.stream().filter(l ->
                l.getDateFin() != null &&
                !l.getDateFin().isBefore(today) &&
                !l.getDateFin().isAfter(in30Days)).count();
        long paiementsAttente = allContrats.stream().filter(c -> "en attente".equalsIgnoreCase(c.getStatut())).count();
        long licencesSuspendues = allLicences.stream().filter(l -> "suspendu".equalsIgnoreCase(l.getStatut())).count();

        // ── Area chart: activations per month (last 6 months) ─
        List<DashboardDTO.EvolutionPoint> evolution = buildEvolution(allLicences, today);

        // ── Alerts ────────────────────────────────────────────
        List<DashboardDTO.AlertItem> alerts = buildAlerts(allLicences, allContrats, today, in30Days);

        return new DashboardDTO(
                totalClients, licencesActives, licencesExpirees,
                contratsEnCours, expirantBientot, paiementsAttente,
                licencesSuspendues, evolution, alerts
        );
    }

    // ── Build last 6 months area chart data ───────────────────
    private List<DashboardDTO.EvolutionPoint> buildEvolution(List<Licence> licences, LocalDate today) {
        List<DashboardDTO.EvolutionPoint> points = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            LocalDate month     = today.minusMonths(i).withDayOfMonth(1);
            LocalDate nextMonth = month.plusMonths(1);
            String    label     = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH)
                                       + " " + month.getYear();

            long count = licences.stream().filter(l ->
                    l.getDateDebut() != null &&
                    !l.getDateDebut().isBefore(month) &&
                    l.getDateDebut().isBefore(nextMonth)
            ).count();

            points.add(new DashboardDTO.EvolutionPoint(label, count));
        }
        return points;
    }

    // ── Build alerts list from real data ──────────────────────
    private List<DashboardDTO.AlertItem> buildAlerts(
            List<Licence> licences, List<ContratAssistance> contrats,
            LocalDate today, LocalDate in30Days) {

        List<DashboardDTO.AlertItem> alerts = new ArrayList<>();

        // Expired licences → danger
        licences.stream()
                .filter(l -> l.getDateFin() != null && l.getDateFin().isBefore(today))
                .limit(3)
                .forEach(l -> alerts.add(new DashboardDTO.AlertItem(
                        "danger",
                        "La licence " + l.getCleLicence() + " a expiré le " + l.getDateFin() + "."
                )));

        // Contracts expiring soon → warning
        contrats.stream()
                .filter(c -> c.getDateFin() != null
                        && !c.getDateFin().isBefore(today)
                        && !c.getDateFin().isAfter(in30Days))
                .limit(3)
                .forEach(c -> {
                    long days = today.until(c.getDateFin()).getDays();
                    alerts.add(new DashboardDTO.AlertItem(
                            "warning",
                            "Le contrat " + c.getNumeroContrat() + " expire dans " + days + " jours."
                    ));
                });

        // New contracts (created this month) → info
        contrats.stream()
                .filter(c -> c.getDateDebut() != null
                        && c.getDateDebut().getMonth() == today.getMonth()
                        && c.getDateDebut().getYear() == today.getYear())
                .limit(2)
                .forEach(c -> alerts.add(new DashboardDTO.AlertItem(
                        "info",
                        "Nouveau contrat signé : " + c.getNumeroContrat() + "."
                )));

        // If no alerts, add a default success message
        if (alerts.isEmpty()) {
            alerts.add(new DashboardDTO.AlertItem("success", "Tout est en ordre. Aucune alerte active."));
        }

        return alerts;
    }
}
