package com.example.protetion_logiciels.controller;

import com.example.protetion_logiciels.entity.Logiciel;
import com.example.protetion_logiciels.service.LogicielService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logiciels")
@RequiredArgsConstructor
public class LogicielController {

    private final LogicielService logicielService;

    @GetMapping
    public ResponseEntity<List<Logiciel>> getAll() {
        return ResponseEntity.ok(logicielService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Logiciel> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(logicielService.findById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Logiciel>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(logicielService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<Logiciel> create(@RequestBody Logiciel logiciel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logicielService.create(logiciel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Logiciel> update(@PathVariable Integer id, @RequestBody Logiciel logiciel) {
        return ResponseEntity.ok(logicielService.update(id, logiciel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logicielService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
