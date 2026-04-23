package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.DTO.ContratAssistanceDTO;
import com.example.protetion_logiciels.Service.ContratAssistanceService;
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
    public ResponseEntity<List<ContratAssistanceDTO.Response>> getAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratAssistanceDTO.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContratAssistanceDTO.Response>> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(contratService.findByClientId(clientId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ContratAssistanceDTO.Response>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(contratService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<ContratAssistanceDTO.Response> create(@RequestBody ContratAssistanceDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratAssistanceDTO.Response> update(@PathVariable Integer id, @RequestBody ContratAssistanceDTO.Request req) {
        return ResponseEntity.ok(contratService.update(id, req));
    }

    @PatchMapping("/{id}/renouveler")
    public ResponseEntity<ContratAssistanceDTO.Response> renouveler(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.renouveler(id));
    }

    @PatchMapping("/{id}/suspendre")
    public ResponseEntity<ContratAssistanceDTO.Response> suspendre(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.suspendre(id));
    }

    @PatchMapping("/{id}/reactiver")
    public ResponseEntity<ContratAssistanceDTO.Response> reactiver(@PathVariable Integer id) {
        return ResponseEntity.ok(contratService.reactiver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contratService.delete(id);
        return ResponseEntity.noContent().build();
    }
}