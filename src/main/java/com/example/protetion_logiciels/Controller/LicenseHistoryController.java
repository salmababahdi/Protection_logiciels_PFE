package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.Entity.*;
import com.example.protetion_logiciels.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/licenses-history")
@RequiredArgsConstructor
public class LicenseHistoryController {

    private final LicenseHistoryService licenseHistoryService;

    @GetMapping
    public ResponseEntity<List<LicenseHistory>> getAll() {
        return ResponseEntity.ok(licenseHistoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenseHistory> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(licenseHistoryService.findById(id));
    }

    @GetMapping("/licence/{licenceId}")
    public ResponseEntity<List<LicenseHistory>> getByLicence(@PathVariable Integer licenceId) {
        return ResponseEntity.ok(licenseHistoryService.findByLicenceId(licenceId));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<LicenseHistory>> getByUtilisateur(@PathVariable Integer utilisateurId) {
        return ResponseEntity.ok(licenseHistoryService.findByUtilisateurId(utilisateurId));
    }

    @PostMapping
    public ResponseEntity<LicenseHistory> enregistrer(@RequestBody LicenseHistory history) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licenseHistoryService.enregistrer(history));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        licenseHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
