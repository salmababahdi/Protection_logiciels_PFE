package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.LicenceDTO;
import com.example.protetion_logiciels.Entity.Client;
import com.example.protetion_logiciels.Entity.Licence;
import com.example.protetion_logiciels.Entity.Logiciel;
import com.example.protetion_logiciels.Entity.ContratAssistance;
import com.example.protetion_logiciels.Repository.LicenceRepository;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.LogicielRepository;
import com.example.protetion_logiciels.Repository.ContratAssistanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenceService {

    private final LicenceRepository licenceRepository;
    private final ClientRepository clientRepository;
    private final LogicielRepository logicielRepository;
    private final ContratAssistanceRepository contratRepository;
    private final LicenseNotificationService notificationService;

    // ── Mapping helpers ───────────────────────────────────────────────────

    private LicenceDTO.Response toResponse(Licence l) {
        LicenceDTO.Response dto = new LicenceDTO.Response();
        dto.setId(l.getId());
        dto.setCleLicence(l.getCleLicence());
        dto.setCodeDebridage(l.getCodeDebridage());
        dto.setNbPostesAutorises(l.getNbPostesAutorises());
        dto.setNbPostesActuels(l.getNbPostesActuels());
        dto.setDateDebut(l.getDateDebut());
        dto.setDateFin(l.getDateFin());
        dto.setStatut(l.getStatut());
        if (l.getClient() != null) {
            dto.setClientId(l.getClient().getId());
            dto.setClientNom(l.getClient().getRaisonSociale());
        }
        if (l.getLogiciel() != null) {
            dto.setLogicielId(l.getLogiciel().getId());
            dto.setLogicielNom(l.getLogiciel().getNom());
        }
        if (l.getContrat() != null) {
            dto.setContratId(l.getContrat().getId());
        }
        return dto;
    }

    private Licence toEntity(LicenceDTO.Request req) {
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + req.getClientId()));
        Logiciel logiciel = logicielRepository.findById(req.getLogicielId())
                .orElseThrow(() -> new EntityNotFoundException("Logiciel not found with id: " + req.getLogicielId()));
        ContratAssistance contrat = contratRepository.findById(req.getContratId())
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + req.getContratId()));

        Licence l = new Licence();
        l.setClient(client);
        l.setLogiciel(logiciel);
        l.setContrat(contrat);
        l.setCleLicence(req.getCleLicence());
        l.setCodeDebridage(req.getCodeDebridage());
        l.setNbPostesAutorises(req.getNbPostesAutorises());
        l.setNbPostesActuels(req.getNbPostesActuels() != null ? req.getNbPostesActuels() : 0);
        l.setDateDebut(req.getDateDebut());
        l.setDateFin(req.getDateFin());
        l.setStatut(req.getStatut() != null ? req.getStatut() : "actif");
        return l;
    }

    // ── Service methods ───────────────────────────────────────────────────

    public List<LicenceDTO.Response> findAll() {
        return licenceRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LicenceDTO.Response findById(Integer id) {
        return toResponse(licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id)));
    }

    public List<LicenceDTO.Response> findByClientId(Integer clientId) {
        return licenceRepository.findByClientId(clientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LicenceDTO.Response> findByLogicielId(Integer logicielId) {
        return licenceRepository.findByLogicielId(logicielId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LicenceDTO.Response> findByContratId(Integer contratId) {
        return licenceRepository.findByContratId(contratId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LicenceDTO.Response> findByStatut(String statut) {
        return licenceRepository.findByStatut(statut)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LicenceDTO.Response create(LicenceDTO.Request req) {
        return toResponse(licenceRepository.save(toEntity(req)));
    }

    public LicenceDTO.Response update(Integer id, LicenceDTO.Request req) {
        Licence existing = licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + req.getClientId()));
        Logiciel logiciel = logicielRepository.findById(req.getLogicielId())
                .orElseThrow(() -> new EntityNotFoundException("Logiciel not found with id: " + req.getLogicielId()));
        ContratAssistance contrat = contratRepository.findById(req.getContratId())
                .orElseThrow(() -> new EntityNotFoundException("Contrat not found with id: " + req.getContratId()));

        String oldStatut = existing.getStatut();
        existing.setClient(client);
        existing.setLogiciel(logiciel);
        existing.setContrat(contrat);
        existing.setCleLicence(req.getCleLicence());
        existing.setCodeDebridage(req.getCodeDebridage());
        existing.setNbPostesAutorises(req.getNbPostesAutorises());
        existing.setNbPostesActuels(req.getNbPostesActuels());
        existing.setDateDebut(req.getDateDebut());
        existing.setDateFin(req.getDateFin());
        existing.setStatut(req.getStatut());
        Licence saved = licenceRepository.save(existing);

        // Notify if status changed to expired or suspended
        notificationService.notifyLicenseStatusChange(saved, oldStatut, req.getStatut());

        return toResponse(saved);
    }

    public LicenceDTO.Response activer(Integer id) {
        Licence entity = licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
        String oldStatut = entity.getStatut();
        entity.setStatut("actif");
        Licence saved = licenceRepository.save(entity);
        notificationService.notifyLicenseStatusChange(saved, oldStatut, "actif");
        return toResponse(saved);
    }

    public LicenceDTO.Response desactiver(Integer id) {
        Licence entity = licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
        String oldStatut = entity.getStatut();
        entity.setStatut("inactif");
        Licence saved = licenceRepository.save(entity);
        notificationService.notifyLicenseStatusChange(saved, oldStatut, "inactif");
        return toResponse(saved);
    }

    public LicenceDTO.Response suspendre(Integer id, String motif) {
        Licence entity = licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
        String oldStatut = entity.getStatut();
        entity.setStatut("suspendu");
        Licence saved = licenceRepository.save(entity);
        notificationService.notifyLicenseStatusChange(saved, oldStatut, "suspendu");
        return toResponse(saved);
    }

    public LicenceDTO.Response reactiver(Integer id) {
        Licence entity = licenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licence not found with id: " + id));
        String oldStatut = entity.getStatut();
        entity.setStatut("actif");
        Licence saved = licenceRepository.save(entity);
        notificationService.notifyLicenseStatusChange(saved, oldStatut, "actif");
        return toResponse(saved);
    }

    public void delete(Integer id) {
        findById(id);
        licenceRepository.deleteById(id);
    }
}
