package com.example.protetion_logiciels.Repository;

import com.example.protetion_logiciels.Entity.Licence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Integer> {
    Optional<Licence> findByCleLicence(String cleLicence);
    List<Licence> findByClientId(Integer clientId);
    List<Licence> findByLogicielId(Integer logicielId);
    List<Licence> findByContratId(Integer contratId);
    List<Licence> findByStatut(String statut);
}