package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.Logiciel;
import com.example.protetion_logiciels.repository.LogicielRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogicielService {

    private final LogicielRepository logicielRepository;

    public List<Logiciel> findAll() {
        return logicielRepository.findAll();
    }

    public Logiciel findById(Integer id) {
        return logicielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logiciel not found with id: " + id));
    }

    public List<Logiciel> findByStatut(String statut) {
        return logicielRepository.findByStatut(statut);
    }

    public Logiciel create(Logiciel logiciel) {
        return logicielRepository.save(logiciel);
    }

    public Logiciel update(Integer id, Logiciel updated) {
        Logiciel existing = findById(id);
        existing.setCodeLogiciel(updated.getCodeLogiciel());
        existing.setNom(updated.getNom());
        existing.setVersion(updated.getVersion());
        existing.setDescription(updated.getDescription());
        existing.setStatut(updated.getStatut());
        return logicielRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        logicielRepository.deleteById(id);
    }
}
