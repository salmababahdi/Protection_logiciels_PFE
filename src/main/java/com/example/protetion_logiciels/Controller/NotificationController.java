package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.DTO.NotificationDTO;
import com.example.protetion_logiciels.Entity.Notification;
import com.example.protetion_logiciels.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAll() {
        List<NotificationDTO> dtos = notificationService.findAll().stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable Integer id) {
        Notification notification = notificationService.findById(id);
        return ResponseEntity.ok(NotificationDTO.fromEntity(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getByUser(@PathVariable Integer userId) {
        List<NotificationDTO> dtos = notificationService.findByUserId(userId).stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/contrat/{contratId}")
    public ResponseEntity<List<NotificationDTO>> getByContrat(@PathVariable Integer contratId) {
        List<NotificationDTO> dtos = notificationService.findByContratId(contratId).stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setTitre(dto.getTitre());
        notification.setDescription(dto.getDescription());
        notification.setType(dto.getType() != null ? dto.getType() : "message");
        notification.setStatut(dto.getStatut() != null ? dto.getStatut() : "Non lu");
        // Note: user and contrat should be set via service methods
        Notification created = notificationService.create(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(NotificationDTO.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> update(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Notification existing = notificationService.findById(id);

        if (updates.containsKey("titre")) {
            existing.setTitre((String) updates.get("titre"));
        }
        if (updates.containsKey("description")) {
            existing.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("type")) {
            existing.setType((String) updates.get("type"));
        }
        if (updates.containsKey("statut")) {
            existing.setStatut((String) updates.get("statut"));
        }

        Notification updated = notificationService.update(id, existing);
        return ResponseEntity.ok(NotificationDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
