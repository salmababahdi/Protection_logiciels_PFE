package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.ChangePasswordRequest;
import com.example.protetion_logiciels.DTO.UpdateProfileRequest;
import com.example.protetion_logiciels.DTO.UserProfileDTO;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParametresService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Helper: entity → DTO ──────────────────────────────────
    private UserProfileDTO toDTO(User u) {
        return new UserProfileDTO(
            u.getId(),
            u.getPrenom() + " " + u.getNom(),
            u.getEmail(),
            u.getUsername(),
            u.getRole() != null ? u.getRole().getNom() : "",
            u.getActif()
        );
    }

    // ── Get profile by email (from JWT) ───────────────────────
    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        return toDTO(user);
    }

    // ── Update name + email ───────────────────────────────────
    public UserProfileDTO updateProfile(String email, UpdateProfileRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // Check new email not already taken by someone else
        if (!email.equals(req.getEmail())) {
            userRepository.findByEmail(req.getEmail()).ifPresent(other -> {
                if (!other.getId().equals(user.getId())) {
                    throw new RuntimeException("Cet email est déjà utilisé par un autre compte.");
                }
            });
        }

        // Split "prenom nom" → prenom + nom
        String[] parts = req.getName().trim().split(" ", 2);
        user.setPrenom(parts[0]);
        user.setNom(parts.length > 1 ? parts[1] : parts[0]);
        user.setEmail(req.getEmail());
        user.setUsername(req.getEmail());

        return toDTO(userRepository.save(user));
    }

    // ── Change password ───────────────────────────────────────
    public void changePassword(String email, ChangePasswordRequest req) {
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Mot de passe actuel incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }
}
