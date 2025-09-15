package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.CollectionRequestResponseDTO;
import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.service.CollectionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
}