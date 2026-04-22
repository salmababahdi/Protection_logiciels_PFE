package com.example.protetion_logiciels.controller;

import com.example.protetion_logiciels.entity.Licence;
import com.example.protetion_logiciels.service.LicenceService;
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
    public ResponseEntity<List<Licence>> getAll() {
        return ResponseEntity.ok(licenceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Licence> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Licence>> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(licenceService.findByClientId(clientId));
    }

    @GetMapping("/logiciel/{logicielId}")
    public ResponseEntity<List<Licence>> getByLogiciel(@PathVariable Integer logicielId) {
        return ResponseEntity.ok(licenceService.findByLogicielId(logicielId));
    }

    @GetMapping("/contrat/{contratId}")
    public ResponseEntity<List<Licence>> getByContrat(@PathVariable Integer contratId) {
        return ResponseEntity.ok(licenceService.findByContratId(contratId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Licence>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(licenceService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<Licence> create(@RequestBody Licence licence) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licenceService.create(licence));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Licence> update(@PathVariable Integer id, @RequestBody Licence licence) {
        return ResponseEntity.ok(licenceService.update(id, licence));
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Licence> activer(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.activer(id));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Licence> desactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.desactiver(id));
    }

    @PatchMapping("/{id}/suspendre")
    public ResponseEntity<Licence> suspendre(@PathVariable Integer id,
                                             @RequestParam(required = false) String motif) {
        return ResponseEntity.ok(licenceService.suspendre(id, motif));
    }

    @PatchMapping("/{id}/reactiver")
    public ResponseEntity<Licence> reactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(licenceService.reactiver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        licenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
