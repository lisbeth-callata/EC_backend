package com.ecocollet.backend.service;

import com.ecocollet.backend.dto.ProfileDTO;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.model.CollectionRequest;  // ← AGREGAR IMPORT
import com.ecocollet.backend.repository.CollectionRequestRepository;
import com.ecocollet.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired // ← AGREGAR ESTO
    private CollectionRequestRepository collectionRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        }).orElse(null);
    }

    public ProfileDTO getUserProfileWithStats(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // CONSULTA REAL para estadísticas
            int totalRequests = collectionRequestRepository.countByUserId(userId);
            Double totalWeight = collectionRequestRepository.sumWeightByUserId(userId);

            // Si totalWeight es null (no hay solicitudes), usar 0.0
            double weight = totalWeight != null ? totalWeight : 0.0;

            return new ProfileDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name(),
                    totalRequests,
                    weight
            );
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUsersWithRequests() {
        return userRepository.findByRequestsIsNotEmpty();
    }

    public List<User> getUsersWithoutRequests() {
        return userRepository.findByRequestsIsEmpty();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}