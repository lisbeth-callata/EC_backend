package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.ProfileDTO;
import com.ecocollet.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            ProfileDTO profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint alternativo para obtener perfil del usuario autenticado
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('COLLECTOR')")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String token) {
        try {
            // Extraer user ID del token (implementaremos esto después)
            // Por ahora usamos un ID hardcodeado para testing
            ProfileDTO profile = userService.getUserProfile(4L); // ID de María Usuario
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}