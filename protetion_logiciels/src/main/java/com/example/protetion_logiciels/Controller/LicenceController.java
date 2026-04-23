package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.DTO.LicenceDTO;
import com.example.protetion_logiciels.Service.LicenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licences")
@RequiredArgsConstructor
public class LicenceController {

    private final LicenceService licenceService;

    @GetMapping
    public ResponseEntity<List<LicenceDTO.Response>> getAll() {
        return ResponseEntity.ok(licenceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenceDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<LicenceDTO.Response>> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(licenceService.findByClientId(clientId));
    }

    @GetMapping("/logiciel/{logicielId}")
    public ResponseEntity<List<LicenceDTO.Response>> getByLogiciel(@PathVariable Integer logicielId) {
        return ResponseEntity.ok(licenceService.findByLogicielId(logicielId));
    }

    @GetMapping("/contrat/{contratId}")
    public ResponseEntity<List<LicenceDTO.Response>> getByContrat(@PathVariable Integer contratId) {
        return ResponseEntity.ok(licenceService.findByContratId(contratId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<LicenceDTO.Response>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(licenceService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<LicenceDTO.Response> create(@RequestBody LicenceDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licenceService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LicenceDTO.Response> update(@PathVariable Integer id, @RequestBody LicenceDTO.Request req) {
        return ResponseEntity.ok(licenceService.update(id, req));
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<LicenceDTO.Response> activer(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.activer(id));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<LicenceDTO.Response> desactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.desactiver(id));
    }

    @PatchMapping("/{id}/suspendre")
    public ResponseEntity<LicenceDTO.Response> suspendre(@PathVariable Integer id,
                                                         @RequestParam(required = false) String motif) {
        return ResponseEntity.ok(licenceService.suspendre(id, motif));
    }

    @PatchMapping("/{id}/reactiver")
    public ResponseEntity<LicenceDTO.Response> reactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.reactiver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        licenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
