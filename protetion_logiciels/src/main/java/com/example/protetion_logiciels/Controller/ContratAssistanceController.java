package com.example.protetion_logiciels.controller;

import com.example.protetion_logiciels.entity.ContratAssistance;
import com.example.protetion_logiciels.service.ContratAssistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratAssistanceController {

    private final ContratAssistanceService contratService;

    @GetMapping
    public ResponseEntity<List<ContratAssistance>> getAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratAssistance> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContratAssistance>> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(contratService.findByClientId(clientId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ContratAssistance>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(contratService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<ContratAssistance> create(@RequestBody ContratAssistance contrat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(contrat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratAssistance> update(@PathVariable Integer id, @RequestBody ContratAssistance contrat) {
        return ResponseEntity.ok(contratService.update(id, contrat));
    }

    @PatchMapping("/{id}/renouveler")
    public ResponseEntity<ContratAssistance> renouveler(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.renouveler(id));
    }

    @PatchMapping("/{id}/suspendre")
    public ResponseEntity<ContratAssistance> suspendre(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.suspendre(id));
    }

    @PatchMapping("/{id}/reactiver")
    public ResponseEntity<ContratAssistance> reactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.reactiver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contratService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
