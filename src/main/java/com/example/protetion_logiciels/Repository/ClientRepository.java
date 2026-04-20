package com.example.protetion_logiciels.Repository;

import com.example.protetion_logiciels.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByCodeClient(String codeClient);
    List<Client> findByStatut(String statut);
}
