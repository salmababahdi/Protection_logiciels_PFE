package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.Entity.Notification;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Entity.ContratAssistance;
import com.example.protetion_logiciels.Repository.NotificationRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Notification findById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
    }

    public List<Notification> findByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> findByContratId(Integer contratId) {
        return notificationRepository.findByContratId(contratId);
    }

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Create a notification for a specific user
     */
    public Notification createForUser(Integer userId, String titre, String description, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setDescription(description);
        notification.setType(type != null ? type : "message");
        notification.setStatut("Non lu");
        notification.setUser(user);
        return notificationRepository.save(notification);
    }

    /**
     * Create a notification linked to a contract
     */
    public Notification createForContract(Integer userId, Integer contratId, String titre, String description, String type) {
        Notification notification = createForUser(userId, titre, description, type);
        if (contratId != null) {
            // Lazy load contrat - would need ContratAssistanceRepository if used
            // For now, keep it simple
        }
        return notification;
    }

    /**
     * Create notification for expired license
     */
    public Notification createLicenseExpiredNotification(Integer userId, String licenceCle, String clientNom, String logicielNom) {
        String titre = "Licence expirée";
        String description = "La licence " + licenceCle + " pour " + logicielNom + " (client: " + clientNom + ") a expiré.";
        return createForUser(userId, titre, description, "alerte");
    }

    /**
     * Create notification for license expiring soon (within 7 days)
     */
    public Notification createLicenseExpiringSoonNotification(Integer userId, String licenceCle, String clientNom, String logicielNom, LocalDate dateFin) {
        String titre = "Licence即将到期";
        String description = "La licence " + licenceCle + " pour " + logicielNom + " (client: " + clientNom + ") expire le " + dateFin + ".";
        return createForUser(userId, titre, description, "alerte");
    }

    /**
     * Create notification for new client
     */
    public Notification createNewClientNotification(Integer userId, String clientNom) {
        String titre = "Nouveau client";
        String description = "Le client " + clientNom + " a été ajouté au système.";
        return createForUser(userId, titre, description, "message");
    }

    /**
     * Create notification for new contract
     */
    public Notification createNewContractNotification(Integer userId, String contratNumero, String clientNom) {
        String titre = "Nouveau contrat";
        String description = "Le contrat " + contratNumero + " a été signé avec " + clientNom + ".";
        return createForUser(userId, titre, description, "message");
    }

    public Notification update(Integer id, Notification updated) {
        Notification existing = findById(id);
        if (updated.getTitre() != null) {
            existing.setTitre(updated.getTitre());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getType() != null) {
            existing.setType(updated.getType());
        }
        if (updated.getStatut() != null) {
            existing.setStatut(updated.getStatut());
        }
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getContrat() != null) {
            existing.setContrat(updated.getContrat());
        }
        return notificationRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        notificationRepository.deleteById(id);
    }
}
