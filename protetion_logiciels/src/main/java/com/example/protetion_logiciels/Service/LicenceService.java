package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.Licence;
import com.example.protetion_logiciels.repository.LicenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenceService {

    private final LicenceRepository licenceRepository;

    public List<Licence> findAll() {
        return licenceRepository.findAll();
    }

    public Licence findById(Integer id) {
        return licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
    }

    public List<Licence> findByClientId(Integer clientId) {
        return licenceRepository.findByClientId(clientId);
    }

    public List<Licence> findByLogicielId(Integer logicielId) {
        return licenceRepository.findByLogicielId(logicielId);
    }

    public List<Licence> findByContratId(Integer contratId) {
        return licenceRepository.findByContratId(contratId);
    }

    public List<Licence> findByStatut(String statut) {
        return licenceRepository.findByStatut(statut);
    }

    public Licence create(Licence licence) {
        return licenceRepository.save(licence);
    }

    public Licence update(Integer id, Licence updated) {
        Licence existing = findById(id);
        existing.setCleLicence(updated.getCleLicence());
        existing.setCodeDebridage(updated.getCodeDebridage());
        existing.setClient(updated.getClient());
        existing.setLogiciel(updated.getLogiciel());
        existing.setContrat(updated.getContrat());
        existing.setNbPostesAutorises(updated.getNbPostesAutorises());
        existing.setNbPostesActuels(updated.getNbPostesActuels());
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        existing.setStatut(updated.getStatut());
        return licenceRepository.save(existing);
    }

    public Licence activer(Integer id) {
        Licence licence = findById(id);
        licence.setStatut("actif");
        return licenceRepository.save(licence);
    }

    public Licence desactiver(Integer id) {
        Licence licence = findById(id);
        licence.setStatut("inactif");
        return licenceRepository.save(licence);
    }

    public Licence suspendre(Integer id, String motif) {
        Licence licence = findById(id);
        licence.setStatut("suspendu");
        return licenceRepository.save(licence);
    }

    public Licence reactiver(Integer id) {
        Licence licence = findById(id);
        licence.setStatut("actif");
        return licenceRepository.save(licence);
    }

    public void delete(Integer id) {
        findById(id);
        licenceRepository.deleteById(id);
    }
}
