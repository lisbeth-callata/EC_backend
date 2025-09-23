package com.ecocollet.backend.service;

import com.ecocollet.backend.dto.ProfileDTO;
import com.ecocollet.backend.dto.WeightSummaryDTO;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
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

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmailOrUsername(String identifier) {
        return userRepository.findByEmailOrUsername(identifier);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setLastname(userDetails.getLastname());
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPhone(userDetails.getPhone());
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        }).orElse(null);
    }

    public ProfileDTO getUserProfile(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int totalRequests = userRepository.countRequestsByUserId(userId);
            double totalWeight = userRepository.sumWeightByUserId(userId);

            return new ProfileDTO(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getLastname(),
                    user.getPhone(),
                    user.getEmail(),
                    user.getRole().name(),
                    totalRequests,
                    totalWeight
            );
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + userId);
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

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.searchUsers("%" + searchTerm + "%");
    }

    public List<User> getActiveCollectors() {
        return userRepository.findByRole(Role.ROLE_COLLECTOR);
    }
    public List<WeightSummaryDTO> getUsersWeightSummary() {
        return userRepository.findUsersWithWeightSummary();
    }
}