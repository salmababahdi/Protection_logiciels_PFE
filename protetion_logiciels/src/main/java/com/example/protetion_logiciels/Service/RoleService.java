package com.example.protetion_logiciels.service;

import com.example.protetion_logiciels.entity.Role;
import com.example.protetion_logiciels.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
    }

    public Role create(Role role) {
        return roleRepository.save(role);
    }

    public Role update(Integer id, Role updated) {
        Role existing = findById(id);
        existing.setNom(updated.getNom());
        existing.setDescription(updated.getDescription());
        return roleRepository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        roleRepository.deleteById(id);
    }
}
