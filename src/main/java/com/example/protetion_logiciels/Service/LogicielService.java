package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.LogicielDTO;
import com.example.protetion_logiciels.Entity.Logiciel;
import com.example.protetion_logiciels.Repository.LogicielRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogicielService {

    private final LogicielRepository logicielRepository;

    // ── Entity → DTO ─────────────────────────────────────────
    private LogicielDTO toDTO(Logiciel l) {
        return new LogicielDTO(
                l.getId(),
                l.getCodeLogiciel(),
                l.getNom(),
                l.getVersion(),
                l.getDescription(),
                l.getStatut()
        );
    }

    // ── DTO → Entity ─────────────────────────────────────────
    private void applyDTO(Logiciel logiciel, LogicielDTO dto) {
        logiciel.setCodeLogiciel(dto.getCodeLogiciel());
        logiciel.setNom(dto.getNom());
        logiciel.setVersion(dto.getVersion());
        logiciel.setDescription(dto.getDescription());
        logiciel.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
    }

    public List<LogicielDTO> findAll() {
        return logicielRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LogicielDTO findById(Integer id) {
        return toDTO(logicielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logiciel not found with id: " + id)));
    }

    public List<LogicielDTO> findByStatut(String statut) {
        return logicielRepository.findByStatut(statut).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LogicielDTO create(LogicielDTO dto) {
        Logiciel logiciel = new Logiciel();
        applyDTO(logiciel, dto);
        return toDTO(logicielRepository.save(logiciel));
    }

    public LogicielDTO update(Integer id, LogicielDTO dto) {
        Logiciel existing = logicielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logiciel not found with id: " + id));
        applyDTO(existing, dto);
        return toDTO(logicielRepository.save(existing));
    }

    public void delete(Integer id) {
        if (!logicielRepository.existsById(id))
            throw new EntityNotFoundException("Logiciel not found with id: " + id);
        logicielRepository.deleteById(id);
    }
}