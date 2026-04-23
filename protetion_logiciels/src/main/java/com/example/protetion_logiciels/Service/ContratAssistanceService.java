package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.ContratAssistanceDTO;
import com.example.protetion_logiciels.Entity.Client;
import com.example.protetion_logiciels.Entity.ContratAssistance;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.ContratAssistanceRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratAssistanceService {

    private final ContratAssistanceRepository contratRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ── Mapping helpers ───────────────────────────────────────────────────

    private ContratAssistanceDTO.Response toResponse(ContratAssistance c) {
        ContratAssistanceDTO.Response dto = new ContratAssistanceDTO.Response();
        dto.setId(c.getId());
        dto.setNumeroContrat(c.getNumeroContrat());
        dto.setDateDebut(c.getDateDebut());
        dto.setDateFin(c.getDateFin());
        dto.setMontant(c.getMontant());
        dto.setStatut(c.getStatut());
        if (c.getClient() != null) {
            dto.setClientId(c.getClient().getId());
            dto.setClientNom(c.getClient().getRaisonSociale());
        }
        return dto;
    }

    private ContratAssistance toEntity(ContratAssistanceDTO.Request req) {
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + req.getClientId()));
        ContratAssistance c = new ContratAssistance();
        c.setNumeroContrat(req.getNumeroContrat());
        c.setClient(client);
        c.setDateDebut(req.getDateDebut());
        c.setDateFin(req.getDateFin());
        c.setMontant(req.getMontant());
        c.setStatut(req.getStatut() != null ? req.getStatut() : "actif");
        return c;
    }

    // ── Service methods ───────────────────────────────────────────────────

    public List<ContratAssistanceDTO.Response> findAll() {
        return contratRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ContratAssistanceDTO.Response findById(Integer id) {
        return toResponse(contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id)));
    }

    public List<ContratAssistanceDTO.Response> findByClientId(Integer clientId) {
        return contratRepository.findByClientId(clientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ContratAssistanceDTO.Response> findByStatut(String statut) {
        return contratRepository.findByStatut(statut)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ContratAssistanceDTO.Response create(ContratAssistanceDTO.Request req) {
        ContratAssistance saved = contratRepository.save(toEntity(req));

        // Create notification for all admin users
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && "admin".equalsIgnoreCase(u.getRole().getNom()))
                .toList();
        for (User admin : admins) {
            notificationService.createNewContractNotification(admin.getId(), saved.getNumeroContrat(), saved.getClient().getRaisonSociale());
        }

        return toResponse(saved);
    }

    public ContratAssistanceDTO.Response update(Integer id, ContratAssistanceDTO.Request req) {
        ContratAssistance existing = contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + req.getClientId()));
        existing.setNumeroContrat(req.getNumeroContrat());
        existing.setClient(client);
        existing.setDateDebut(req.getDateDebut());
        existing.setDateFin(req.getDateFin());
        existing.setMontant(req.getMontant());
        existing.setStatut(req.getStatut());
        return toResponse(contratRepository.save(existing));
    }

    public ContratAssistanceDTO.Response renouveler(Integer id) {
        ContratAssistance c = contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
        c.setStatut("renouvelé");
        return toResponse(contratRepository.save(c));
    }

    public ContratAssistanceDTO.Response suspendre(Integer id) {
        ContratAssistance c = contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
        c.setStatut("suspendu");
        return toResponse(contratRepository.save(c));
    }

    public ContratAssistanceDTO.Response reactiver(Integer id) {
        ContratAssistance c = contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
        c.setStatut("actif");
        return toResponse(contratRepository.save(c));
    }

    public void delete(Integer id) {
        contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + id));
        contratRepository.deleteById(id);
    }
}