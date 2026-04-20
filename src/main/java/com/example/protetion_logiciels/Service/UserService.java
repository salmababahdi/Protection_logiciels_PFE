package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Integer id, User updated) {
        User existing = findById(id);
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setEmail(updated.getEmail());
        existing.setUsername(updated.getUsername());
        existing.setRole(updated.getRole());
        existing.setActif(updated.getActif());
        return userRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        userRepository.deleteById(id);
    }
}
