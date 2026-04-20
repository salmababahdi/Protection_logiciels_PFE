package com.example.protetion_logiciels.Controller;

import com.example.protetion_logiciels.DTO.ChangePasswordRequest;
import com.example.protetion_logiciels.DTO.UpdateProfileRequest;
import com.example.protetion_logiciels.DTO.UserProfileDTO;
import com.example.protetion_logiciels.Service.ParametresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/parametres")
@RequiredArgsConstructor
public class ParametresController {

    private final ParametresService parametresService;

    // GET /api/parametres/profile
    // Principal is injected by Spring Security from the JWT token automatically
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Principal principal) {
        return ResponseEntity.ok(parametresService.getProfile(principal.getName()));
    }

    // PUT /api/parametres/profile
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            Principal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(parametresService.updateProfile(principal.getName(), request));
    }

    // PUT /api/parametres/password
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            Principal principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        parametresService.changePassword(principal.getName(), request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès."));
    }
}
