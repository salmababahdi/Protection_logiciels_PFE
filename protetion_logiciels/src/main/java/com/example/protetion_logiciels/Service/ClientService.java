package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.Client;
import com.example.protetion_logiciels.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
    }

    public List<Client> findByStatut(String statut) {
        return clientRepository.findByStatut(statut);
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Integer id, Client updated) {
        Client existing = findById(id);
        existing.setCodeClient(updated.getCodeClient());
        existing.setRaisonSociale(updated.getRaisonSociale());
        existing.setSiret(updated.getSiret());
        existing.setEmail(updated.getEmail());
        existing.setTelephone(updated.getTelephone());
        existing.setAdresse(updated.getAdresse());
        existing.setVille(updated.getVille());
        existing.setStatut(updated.getStatut());
        return clientRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        clientRepository.deleteById(id);
    }
}
