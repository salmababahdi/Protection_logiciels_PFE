package com.example.protetion_logiciels.Repository;

import com.example.protetion_logiciels.Entity.ContratAssistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratAssistanceRepository extends JpaRepository<ContratAssistance, Integer> {
    Optional<ContratAssistance> findByNumeroContrat(String numeroContrat);
    List<ContratAssistance> findByClientId(Integer clientId);
    List<ContratAssistance> findByStatut(String statut);
}