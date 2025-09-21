package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.AssignmentRequestDTO;
import com.ecocollet.backend.dto.AssignmentResponseDTO;
import com.ecocollet.backend.model.AssignmentStatus;
import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.service.CollectionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AssignmentController {

    @Autowired
    private CollectionRequestService collectionRequestService;

    // Reclamar una solicitud disponible
    @PostMapping("/claim/{requestId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> claimRequest(@PathVariable Long requestId,
                                          @RequestBody AssignmentRequestDTO assignmentRequest) {
        try {
            Optional<CollectionRequest> optionalRequest = collectionRequestService.getRequestById(requestId);

            if (optionalRequest.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            CollectionRequest request = optionalRequest.get();

            // ✅ VERIFICACIÓN SIMPLIFICADA: Solo prevenir asignación si ya está COMPLETADA
            if (request.getAssignmentStatus() == AssignmentStatus.COMPLETED) {
                return ResponseEntity.badRequest().body(
                        new AssignmentResponseDTO(
                                requestId, request.getCode(), null, null,
                                request.getAssignmentStatus(), null, null,
                                "La solicitud ya ha sido completada"
                        )
                );
            }

            // ✅ Asignar la solicitud al recolector (estado informativo)
            request.setAssignedCollectorId(assignmentRequest.getCollectorId());
            request.setAssignedCollectorName(assignmentRequest.getCollectorName());
            request.setAssignmentStatus(AssignmentStatus.IN_PROGRESS); // ✅ Cambiado a IN_PROGRESS
            request.setAssignedAt(LocalDateTime.now());

            // ✅ Tiempo de expiración más corto (15 min) ya que es solo informativo
            int timeout = assignmentRequest.getTimeoutMinutes() != null ?
                    assignmentRequest.getTimeoutMinutes() : 15;
            request.setAssignmentExpiresAt(LocalDateTime.now().plusMinutes(timeout));

            CollectionRequest updatedRequest = collectionRequestService.updateRequest(requestId, request);

            return ResponseEntity.ok(
                    new AssignmentResponseDTO(
                            updatedRequest.getId(), updatedRequest.getCode(),
                            updatedRequest.getAssignedCollectorId(), updatedRequest.getAssignedCollectorName(),
                            updatedRequest.getAssignmentStatus(), updatedRequest.getAssignedAt(),
                            updatedRequest.getAssignmentExpiresAt(),
                            "✅ En camino: Ahora otros recolectores saben que estás atendiendo esta solicitud"
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Liberar una solicitud asignada
    @PostMapping("/release/{requestId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> releaseRequest(@PathVariable Long requestId) {
        try {
            Optional<CollectionRequest> optionalRequest = collectionRequestService.getRequestById(requestId);

            if (optionalRequest.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            CollectionRequest request = optionalRequest.get();

            // Liberar la asignación
            request.setAssignedCollectorId(null);
            request.setAssignedCollectorName(null);
            request.setAssignmentStatus(AssignmentStatus.AVAILABLE);
            request.setAssignedAt(null);
            request.setAssignmentExpiresAt(null);

            CollectionRequest updatedRequest = collectionRequestService.updateRequest(requestId, request);

            return ResponseEntity.ok(
                    new AssignmentResponseDTO(
                            updatedRequest.getId(), updatedRequest.getCode(),
                            null, null, AssignmentStatus.AVAILABLE, null, null,
                            "Solicitud liberada exitosamente"
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Completar una solicitud asignada
    @PostMapping("/complete/{requestId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> completeRequest(@PathVariable Long requestId) {
        try {
            Optional<CollectionRequest> optionalRequest = collectionRequestService.getRequestById(requestId);

            if (optionalRequest.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            CollectionRequest request = optionalRequest.get();

            if (request.getAssignmentStatus() != AssignmentStatus.PENDING &&
                    request.getAssignmentStatus() != AssignmentStatus.IN_PROGRESS) {
                return ResponseEntity.badRequest().body("La solicitud no está en estado asignado");
            }

            request.setAssignmentStatus(AssignmentStatus.COMPLETED);
            CollectionRequest updatedRequest = collectionRequestService.updateRequest(requestId, request);

            return ResponseEntity.ok(
                    new AssignmentResponseDTO(
                            updatedRequest.getId(), updatedRequest.getCode(),
                            updatedRequest.getAssignedCollectorId(), updatedRequest.getAssignedCollectorName(),
                            AssignmentStatus.COMPLETED, updatedRequest.getAssignedAt(), null,
                            "Solicitud completada exitosamente"
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Obtener solicitudes asignadas a un recolector
    @GetMapping("/collector/{collectorId}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> getCollectorAssignments(@PathVariable Long collectorId) {
        List<CollectionRequest> assignments = collectionRequestService.getRequestsByCollectorId(collectorId);
        return ResponseEntity.ok(assignments);
    }

    // Obtener solicitudes disponibles (no asignadas)
    @GetMapping("/available")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> getAvailableRequests() {
        List<CollectionRequest> availableRequests = collectionRequestService.getAvailableRequests();
        return ResponseEntity.ok(availableRequests);
    }

    // Actualizar estado de una asignación
    @PutMapping("/{requestId}/status/{status}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAssignmentStatus(@PathVariable Long requestId,
                                                    @PathVariable AssignmentStatus status) {
        try {
            Optional<CollectionRequest> optionalRequest = collectionRequestService.getRequestById(requestId);

            if (optionalRequest.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            CollectionRequest request = optionalRequest.get();
            request.setAssignmentStatus(status);

            CollectionRequest updatedRequest = collectionRequestService.updateRequest(requestId, request);

            return ResponseEntity.ok(updatedRequest);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}