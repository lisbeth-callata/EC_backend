package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.CollectionRequestResponseDTO;
import com.ecocollet.backend.model.AssignmentStatus;
import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.service.CollectionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collector")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CollectorController {

    @Autowired
    private CollectionRequestService collectionRequestService;

    // Dashboard del recolector - solicitudes del día
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequestResponseDTO>> getCollectorDashboard() {
        List<CollectionRequest> requests = collectionRequestService.getTodayRequests();

        // Convertir a DTOs
        List<CollectionRequestResponseDTO> dtos = requests.stream()
                .map(request -> new CollectionRequestResponseDTO(
                        request.getId(),
                        request.getCode(),
                        request.getMaterial(),
                        request.getDescription(),
                        request.getLatitude(),
                        request.getLongitude(),
                        request.getAddress(),
                        request.getStatus(),
                        request.getCreatedAt(),
                        request.getUpdatedAt(),
                        request.getWeight(),
                        request.getUser().getId(),
                        request.getUser().getName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/collector/stats")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Integer>> getCollectorStats() {
        Map<String, Integer> stats = new HashMap<>();

        List<CollectionRequest> todayRequests = collectionRequestService.getTodayRequests();
        int total = todayRequests.size();
        int pending = (int) todayRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .count();
        int collected = (int) todayRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.COLLECTED)
                .count();

        stats.put("totalRequests", total);
        stats.put("pendingRequests", pending);
        stats.put("collectedRequests", collected);

        return ResponseEntity.ok(stats);
    }

    // Actualizar peso y estado (para recolectores)
    @PutMapping("/requests/{id}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id,
                                                 @RequestParam Double weight,
                                                 @RequestParam String status) {
        try {
            CollectionRequest request = collectionRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

            request.setWeight(weight);
            request.setStatus(RequestStatus.valueOf(status.toUpperCase()));

            CollectionRequest updated = collectionRequestService.updateRequest(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/requests/{requestId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRequestForCollector(
            @PathVariable Long requestId,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String status) {

        try {
            CollectionRequest request = collectionRequestService.getRequestById(requestId)
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

            // Actualizar peso si se proporciona
            if (weight != null) {
                request.setWeight(weight);
            }

            // Actualizar estado si se proporciona
            if (status != null) {
                try {
                    RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
                    request.setStatus(newStatus);

                    // ✅ Si se marca como COLLECTED, liberar la asignación automáticamente
                    if (newStatus == RequestStatus.COLLECTED) {
                        request.setAssignmentStatus(AssignmentStatus.COMPLETED);
                        request.setAssignedCollectorId(null);
                        request.setAssignedCollectorName(null);
                    }
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Estado no válido: " + status);
                }
            }

            CollectionRequest updatedRequest = collectionRequestService.updateRequest(requestId, request);
            return ResponseEntity.ok(updatedRequest);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}