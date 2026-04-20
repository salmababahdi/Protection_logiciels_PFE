package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.DTO.LogicielDTO;
import com.example.protetion_logiciels.Service.LogicielService;
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
    public ResponseEntity<List<LogicielDTO>> getAll() {
        return ResponseEntity.ok(logicielService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogicielDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(logicielService.findById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<LogicielDTO>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(logicielService.findByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<LogicielDTO> create(@RequestBody LogicielDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logicielService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogicielDTO> update(@PathVariable Integer id, @RequestBody LogicielDTO dto) {
        return ResponseEntity.ok(logicielService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logicielService.delete(id);
        return ResponseEntity.noContent().build();
    }
}