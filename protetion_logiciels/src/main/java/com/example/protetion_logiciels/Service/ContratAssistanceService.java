package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.ContratAssistance;
import com.example.protetion_logiciels.repository.ContratAssistanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratAssistanceService {

    private final ContratAssistanceRepository contratRepository;

    public List<ContratAssistance> findAll() {
        return contratRepository.findAll();
    }

    public ContratAssistance findById(Integer id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
    }

    public List<ContratAssistance> findByClientId(Integer clientId) {
        return contratRepository.findByClientId(clientId);
    }

    public List<ContratAssistance> findByStatut(String statut) {
        return contratRepository.findByStatut(statut);
    }

    public ContratAssistance create(ContratAssistance contrat) {
        return contratRepository.save(contrat);
    }

    public ContratAssistance update(Integer id, ContratAssistance updated) {
        ContratAssistance existing = findById(id);
        existing.setNumeroContrat(updated.getNumeroContrat());
        existing.setClient(updated.getClient());
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        existing.setMontant(updated.getMontant());
        existing.setStatut(updated.getStatut());
        return contratRepository.save(existing);
    }

    public ContratAssistance renouveler(Integer id) {
        ContratAssistance contrat = findById(id);
        contrat.setStatut("renouvelé");
        return contratRepository.save(contrat);
    }

    public ContratAssistance suspendre(Integer id) {
        ContratAssistance contrat = findById(id);
        contrat.setStatut("suspendu");
        return contratRepository.save(contrat);
    }

    public ContratAssistance reactiver(Integer id) {
        ContratAssistance contrat = findById(id);
        contrat.setStatut("actif");
        return contratRepository.save(contrat);
    }

    public void delete(Integer id) {
        findById(id);
        contratRepository.deleteById(id);
    }
}
