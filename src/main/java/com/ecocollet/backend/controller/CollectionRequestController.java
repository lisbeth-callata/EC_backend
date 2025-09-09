package com.ecocollet.backend.controller;

import com.ecocollet.backend.dto.CollectionRequestResponseDTO;
import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.service.CollectionRequestService;
import com.ecocollet.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CollectionRequestController {

    @Autowired
    private CollectionRequestService collectionRequestService;

    @Autowired
    private UserService userService;

    // Obtener todas las solicitudes (solo admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> getAllRequests() {
        return ResponseEntity.ok(collectionRequestService.getAllRequests());
    }

    // Obtener solicitudes por usuario
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> getRequestsByUser(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(collectionRequestService.getRequestsByUser(user.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener solicitudes del día (para recolectores)
    @GetMapping("/today")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> getTodayRequests() {
        return ResponseEntity.ok(collectionRequestService.getTodayRequests());
    }

    // Crear nueva solicitud
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRequest(@RequestBody CollectionRequest request,
                                           @RequestParam Long userId) {
        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                CollectionRequest newRequest = collectionRequestService.createRequest(request, user.get());

                // Crear DTO para la respuesta
                CollectionRequestResponseDTO responseDTO = new CollectionRequestResponseDTO(
                        newRequest.getId(),
                        newRequest.getCode(),
                        newRequest.getMaterial(),
                        newRequest.getDescription(),
                        newRequest.getLatitude(),
                        newRequest.getLongitude(),
                        newRequest.getAddress(),
                        newRequest.getStatus(),
                        newRequest.getCreatedAt(),
                        newRequest.getUpdatedAt(),
                        newRequest.getWeight(),
                        newRequest.getUser().getId(),
                        newRequest.getUser().getName()
                );

                return ResponseEntity.ok(responseDTO);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la solicitud: " + e.getMessage());
        }
    }

    // Actualizar solicitud (para recolectores y admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COLLECTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRequest(@PathVariable Long id,
                                           @RequestBody CollectionRequest requestDetails) {
        try {
            CollectionRequest updatedRequest = collectionRequestService.updateRequest(id, requestDetails);
            if (updatedRequest != null) {
                return ResponseEntity.ok(updatedRequest);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la solicitud: " + e.getMessage());
        }
    }

    // Eliminar solicitud
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        try {
            collectionRequestService.deleteRequest(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la solicitud: " + e.getMessage());
        }
    }

    // Buscar solicitudes por código o nombre
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CollectionRequest>> searchRequests(@RequestParam String term) {
        return ResponseEntity.ok(collectionRequestService.searchRequests(term));
    }
}