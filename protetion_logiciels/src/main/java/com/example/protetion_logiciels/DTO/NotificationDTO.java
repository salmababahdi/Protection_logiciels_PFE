package com.example.protetion_logiciels.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String titre;
    private String description;
    private String type;
    private String statut;
    private LocalDateTime dateCreation;
    private Integer userId;
    private Integer contratId;

    public static NotificationDTO fromEntity(com.example.protetion_logiciels.Entity.Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(n.getId());
        dto.setTitre(n.getTitre());
        dto.setDescription(n.getDescription());
        dto.setType(n.getType());
        dto.setStatut(n.getStatut());
        dto.setDateCreation(n.getDateCreation());
        dto.setUserId(n.getUser() != null ? n.getUser().getId() : null);
        dto.setContratId(n.getContrat() != null ? n.getContrat().getId() : null);
        return dto;
    }
}
