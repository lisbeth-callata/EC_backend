package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.WeightSummaryDTO;
import com.ecocollet.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/weights")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminWeightController {

    @Autowired
    private UserService userService;

    // Obtener resumen de pesos de todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WeightSummaryDTO>> getUsersWeightSummary() {
        try {
            List<WeightSummaryDTO> weightSummary = userService.getUsersWeightSummary();
            return ResponseEntity.ok(weightSummary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener peso total de un usuario específico
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeightSummaryDTO> getUserWeightSummary(@PathVariable Long userId) {
        try {
            List<WeightSummaryDTO> allWeights = userService.getUsersWeightSummary();
            WeightSummaryDTO userWeight = allWeights.stream()
                    .filter(w -> w.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (userWeight != null) {
                return ResponseEntity.ok(userWeight);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Estadísticas generales de peso
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getWeightStats() {
        try {
            List<WeightSummaryDTO> weightSummary = userService.getUsersWeightSummary();

            double totalWeight = weightSummary.stream()
                    .mapToDouble(WeightSummaryDTO::getTotalWeight)
                    .sum();

            int totalUsers = weightSummary.size();
            double averageWeight = totalUsers > 0 ? totalWeight / totalUsers : 0;

            // Crear objeto de respuesta
            WeightStatsResponse stats = new WeightStatsResponse(totalWeight, totalUsers, averageWeight);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Clase interna para la respuesta de estadísticas
    public static class WeightStatsResponse {
        private double totalWeight;
        private int totalUsers;
        private double averageWeight;

        public WeightStatsResponse(double totalWeight, int totalUsers, double averageWeight) {
            this.totalWeight = totalWeight;
            this.totalUsers = totalUsers;
            this.averageWeight = averageWeight;
        }

        // Getters
        public double getTotalWeight() { return totalWeight; }
        public int getTotalUsers() { return totalUsers; }
        public double getAverageWeight() { return averageWeight; }
    }
}