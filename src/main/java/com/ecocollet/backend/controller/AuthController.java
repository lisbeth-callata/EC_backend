package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.AuthResponse;
import com.ecocollet.backend.dto.LoginRequest;
import com.ecocollet.backend.dto.RegisterResponse;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.security.JwtUtils;
import com.ecocollet.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Obtener información del usuario
            User user = userService.getUserByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name(),
                    user.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en la autenticación: Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body(
                        new RegisterResponse("Error: El email ya está en uso", null)
                );
            }

            if (user.getRole() == null) {
                user.setRole(Role.ROLE_USER);
            }

            User newUser = userService.createUser(user);

            return ResponseEntity.ok().body(
                    new RegisterResponse("Usuario registrado exitosamente", newUser.getId())
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new RegisterResponse("Error en el registro: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok().body("{\"exists\": " + exists + "}");
    }
}