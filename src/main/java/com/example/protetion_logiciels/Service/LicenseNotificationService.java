package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.Entity.Licence;
import com.example.protetion_logiciels.Entity.Notification;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.LicenceRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseNotificationService {

    private final LicenceRepository licenceRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TransactionTemplate transactionTemplate;

    /**
     * Check for expired licenses and create notifications
     */
    @Transactional
    public void checkAndNotifyExpiredLicenses() {
        LocalDate today = LocalDate.now();
        List<Licence> expiredLicences = licenceRepository.findAll().stream()
                .filter(l -> "expiré".equalsIgnoreCase(l.getStatut()) || l.getDateFin().isBefore(today))
                .toList();

        for (Licence licence : expiredLicences) {
            // Find admin users to notify
            List<User> admins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() != null && "admin".equalsIgnoreCase(u.getRole().getNom()))
                    .toList();

            for (User admin : admins) {
                // Check if notification already exists for this license
                boolean alreadyNotified = notificationService.findByUserId(admin.getId()).stream()
                        .anyMatch(n -> n.getTitre().contains("Licence expirée") &&
                                       n.getDescription().contains(licence.getCleLicence()));

                if (!alreadyNotified) {
                    String clientNom = licence.getClient() != null ? licence.getClient().getRaisonSociale() : "Inconnu";
                    String logicielNom = licence.getLogiciel() != null ? licence.getLogiciel().getNom() : "Inconnu";
                    notificationService.createLicenseExpiredNotification(
                            admin.getId(),
                            licence.getCleLicence(),
                            clientNom,
                            logicielNom
                    );
                    log.info("Created expired license notification for admin {} - License: {}",
                            admin.getEmail(), licence.getCleLicence());
                }
            }
        }
    }

    /**
     * Check for licenses expiring soon (within 7 days) and create notifications
     */
    @Transactional
    public void checkAndNotifyExpiringSoonLicenses() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Licence> expiringLicences = licenceRepository.findAll().stream()
                .filter(l -> l.getDateFin() != null &&
                             !l.getDateFin().isBefore(today) &&
                             l.getDateFin().isBefore(nextWeek) &&
                             !"expiré".equalsIgnoreCase(l.getStatut()))
                .toList();

        for (Licence licence : expiringLicences) {
            List<User> admins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() != null && "admin".equalsIgnoreCase(u.getRole().getNom()))
                    .toList();

            for (User admin : admins) {
                boolean alreadyNotified = notificationService.findByUserId(admin.getId()).stream()
                        .anyMatch(n -> n.getTitre().contains("expirer") &&
                                       n.getDescription().contains(licence.getCleLicence()));

                if (!alreadyNotified) {
                    String clientNom = licence.getClient() != null ? licence.getClient().getRaisonSociale() : "Inconnu";
                    String logicielNom = licence.getLogiciel() != null ? licence.getLogiciel().getNom() : "Inconnu";
                    notificationService.createLicenseExpiringSoonNotification(
                            admin.getId(),
                            licence.getCleLicence(),
                            clientNom,
                            logicielNom,
                            licence.getDateFin()
                    );
                    log.info("Created expiring soon notification for admin {} - License: {}",
                            admin.getEmail(), licence.getCleLicence());
                }
            }
        }
    }

    /**
     * Create notification when a license status changes to expired
     */
    public void notifyLicenseStatusChange(Licence licence, String oldStatut, String newStatut) {
        if ("expiré".equalsIgnoreCase(newStatut) || "suspendu".equalsIgnoreCase(newStatut)) {
            List<User> admins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() != null && "admin".equalsIgnoreCase(u.getRole().getNom()))
                    .toList();

            for (User admin : admins) {
                String clientNom = licence.getClient() != null ? licence.getClient().getRaisonSociale() : "Inconnu";
                String logicielNom = licence.getLogiciel() != null ? licence.getLogiciel().getNom() : "Inconnu";

                String titre = "expiré".equalsIgnoreCase(newStatut) ? "Licence expirée" : "Licence suspendue";
                String description = "La licence " + licence.getCleLicence() + " pour " + logicielNom +
                                   " (client: " + clientNom + ") est maintenant " + newStatut + ".";

                notificationService.createForUser(admin.getId(), titre, description, "alerte");
            }
        }
    }

    /**
     * Scheduled task: run every hour to check for expired/expiring licenses
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void scheduledLicenseCheck() {
        log.info("Running scheduled license expiration check...");
        checkAndNotifyExpiredLicenses();
        checkAndNotifyExpiringSoonLicenses();
    }

    /**
     * Run on startup to check licenses
     */
    @PostConstruct
    public void init() {
        log.info("License notification service initialized - will check for expired licenses");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                checkAndNotifyExpiredLicenses();
                checkAndNotifyExpiringSoonLicenses();
            }
        });
    }
}
