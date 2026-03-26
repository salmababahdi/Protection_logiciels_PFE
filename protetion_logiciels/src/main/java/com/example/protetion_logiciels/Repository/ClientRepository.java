package com.example.protetion_logiciels.repository;

import com.example.protetion_logiciels.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByCodeClient(String codeClient);
    List<Client> findByStatut(String statut);
}
