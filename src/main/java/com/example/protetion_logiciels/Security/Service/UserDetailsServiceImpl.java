package com.example.protetion_logiciels.Security;

import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        List<SimpleGrantedAuthority> authorities = List.of();

        if (user.getRole() != null && user.getRole().getNom() != null) {
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().getNom().toUpperCase())
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.getActif(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}