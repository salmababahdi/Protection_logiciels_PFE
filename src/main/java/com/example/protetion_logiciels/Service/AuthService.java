package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.*;
import com.example.protetion_logiciels.Entity.Role;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.RoleRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import com.example.protetion_logiciels.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository        userRepository;
    private final RoleRepository        roleRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtUtils              jwtUtils;

    // ── LOGIN ────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return new AuthResponse(
                token,
                user.getId(),
                user.getNom() + " " + user.getPrenom(),
                user.getEmail(),
                user.getRole().getNom()
        );
    }

    // ── REGISTER ─────────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Un compte existe déjà avec cet email.");
        }

        // Default role: USER (must exist in DB)
        Role defaultRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Rôle par défaut introuvable. Insérez d'abord un rôle avec id=1."));

        // Split name into nom + prenom (first word = prenom, rest = nom)
        String[] parts  = request.getName().trim().split(" ", 2);
        String   prenom = parts[0];
        String   nom    = parts.length > 1 ? parts[1] : parts[0];

        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(defaultRole);
        user.setActif(true);

        userRepository.save(user);

        String token = jwtUtils.generateTokenFromEmail(user.getEmail());

        return new AuthResponse(
                token,
                user.getId(),
                request.getName(),
                user.getEmail(),
                defaultRole.getNom()
        );
    }
}