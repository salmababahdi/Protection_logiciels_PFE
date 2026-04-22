package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.Notification;
import com.example.protetion_logiciels.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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

    public Notification update(Integer id, Notification updated) {
        Notification existing = findById(id);
        existing.setTitre(updated.getTitre());
        existing.setDescription(updated.getDescription());
        existing.setUser(updated.getUser());
        existing.setContrat(updated.getContrat());
        return notificationRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        notificationRepository.deleteById(id);
    }
}
