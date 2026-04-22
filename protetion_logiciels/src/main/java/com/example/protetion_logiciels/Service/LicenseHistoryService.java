package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.LicenseHistory;
import com.example.protetion_logiciels.repository.LicenseHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseHistoryService {

    private final LicenseHistoryRepository licenseHistoryRepository;

    public List<LicenseHistory> findAll() {
        return licenseHistoryRepository.findAll();
    }

    public LicenseHistory findById(Integer id) {
        return licenseHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LicenseHistory not found with id: " + id));
    }

    public List<LicenseHistory> findByLicenceId(Integer licenceId) {
        return licenseHistoryRepository.findByLicenceId(licenceId);
    }

    public List<LicenseHistory> findByUtilisateurId(Integer utilisateurId) {
        return licenseHistoryRepository.findByUtilisateurId(utilisateurId);
    }

    public LicenseHistory enregistrer(LicenseHistory history) {
        history.setDateAction(LocalDateTime.now());
        return licenseHistoryRepository.save(history);
    }

    public void delete(Integer id) {
        findById(id);
        licenseHistoryRepository.deleteById(id);
    }
}
