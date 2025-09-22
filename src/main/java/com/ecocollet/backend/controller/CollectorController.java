package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.CollectionRequestFullDTO;
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

    // Dashboard del recolector - solicitudes del día - MODIFICADO para usar DTO completo
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequestFullDTO>> getCollectorDashboard() {
        List<CollectionRequestFullDTO> dtos = collectionRequestService.getTodayRequestsWithUserInfo();
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

    @PatchMapping("/collector/requests/{requestId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRequestForCollector(
            @PathVariable Long requestId,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String status) {

        try {
            CollectionRequest request = collectionRequestService.getRequestById(requestId)
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

            if (weight != null) {
                request.setWeight(weight);
            }
            if (status != null) {
                try {
                    RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
                    request.setStatus(newStatus);

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

    // NUEVO: Obtener solicitudes asignadas al recolector actual con información completa
    @GetMapping("/my-assignments/{collectorId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequestFullDTO>> getMyAssignments(@PathVariable Long collectorId) {
        List<CollectionRequest> requests = collectionRequestService.getRequestsByCollectorId(collectorId);
        List<CollectionRequestFullDTO> dtos = requests.stream()
                .map(collectionRequestService::convertToFullDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}