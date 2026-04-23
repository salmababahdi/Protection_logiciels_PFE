package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.ClientDTO;
import com.example.protetion_logiciels.Entity.Client;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ── Entity → DTO ─────────────────────────────────────────
    private ClientDTO toDTO(Client c) {
        return new ClientDTO(
                c.getId(), c.getCodeClient(), c.getRaisonSociale(),
                c.getSiret(), c.getEmail(), c.getTelephone(),
                c.getAdresse(), c.getVille(), c.getStatut()
        );
    }

    // ── DTO → Entity ─────────────────────────────────────────
    private void applyDTO(Client client, ClientDTO dto) {
        client.setCodeClient(dto.getCodeClient());
        client.setRaisonSociale(dto.getRaisonSociale());
        client.setSiret(dto.getSiret());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        client.setAdresse(dto.getAdresse());
        client.setVille(dto.getVille());
        client.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClientDTO findById(Integer id) {
        return toDTO(clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id)));
    }

    public List<ClientDTO> findByStatut(String statut) {
        return clientRepository.findByStatut(statut).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClientDTO create(ClientDTO dto) {
        Client client = new Client();
        applyDTO(client, dto);
        Client saved = clientRepository.save(client);

        // Create notification for ALL users (for testing) - you can filter by role later
        List<User> allUsers = userRepository.findAll();
        System.out.println("[ClientService] Creating client: " + saved.getRaisonSociale());
        System.out.println("[ClientService] Total users in DB: " + allUsers.size());

        for (User user : allUsers) {
            String roleName = user.getRole() != null ? user.getRole().getNom() : "NO ROLE";
            System.out.println("[ClientService] User: " + user.getEmail() + " - Role: " + roleName);

            // Create notification for all users (remove role filter for testing)
            notificationService.createNewClientNotification(user.getId(), saved.getRaisonSociale());
            System.out.println("[ClientService] Created notification for user: " + user.getId());
        }

        return toDTO(saved);
    }

    public ClientDTO update(Integer id, ClientDTO dto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        applyDTO(existing, dto);
        return toDTO(clientRepository.save(existing));
    }

    public void delete(Integer id) {
        if (!clientRepository.existsById(id))
            throw new EntityNotFoundException("Client not found with id: " + id);
        clientRepository.deleteById(id);
    }
}