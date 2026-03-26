package com.example.protetion_logiciels.repository;

import com.example.protetion_logiciels.entity.Logiciel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogicielRepository extends JpaRepository<Logiciel, Integer> {
    List<Logiciel> findByStatut(String statut);
}
