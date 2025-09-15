package com.ecocollet.backend.controller;

import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private UserService userService;

    // Dashboard del admin - estadísticas
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("usersWithRequests", userService.getUsersWithRequests().size());
        stats.put("usersWithoutRequests", userService.getUsersWithoutRequests().size());

        return ResponseEntity.ok(stats);
    }

    // Crear usuarios recolectores (solo admin)
    @PostMapping("/collectors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCollector(@RequestBody User collector) {
        try {
            collector.setRole(Role.ROLE_COLLECTOR);
            User newCollector = userService.createUser(collector);
            return ResponseEntity.ok(newCollector);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}