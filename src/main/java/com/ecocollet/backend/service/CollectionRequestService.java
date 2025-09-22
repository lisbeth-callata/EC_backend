package com.ecocollet.backend.service;

import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.repository.CollectionRequestRepository;
import com.ecocollet.backend.dto.CollectionRequestFullDTO;
import com.ecocollet.backend.model.AssignmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CollectionRequestService {

    @Autowired
    private CollectionRequestRepository collectionRequestRepository;

    // Métodos existentes...
    public List<CollectionRequest> getAllRequests() {
        return collectionRequestRepository.findAllWithUser(); // Modificado
    }

    public Optional<CollectionRequest> getRequestById(Long id) {
        return collectionRequestRepository.findById(id);
    }

    public Optional<CollectionRequest> getRequestByCode(String code) {
        return collectionRequestRepository.findByCode(code);
    }

    public List<CollectionRequest> getRequestsByUser(User user) {
        return collectionRequestRepository.findByUser(user);
    }

    public List<CollectionRequest> getRequestsByStatus(RequestStatus status) {
        return collectionRequestRepository.findByStatus(status);
    }

    public List<CollectionRequest> getTodayRequests() {
        return collectionRequestRepository.findTodayRequests();
    }

    public List<CollectionRequest> searchRequests(String searchTerm) {
        return collectionRequestRepository.findByCodeOrUserName(searchTerm);
    }

    public List<CollectionRequest> getRequestsByCollectorId(Long collectorId) {
        return collectionRequestRepository.findByAssignedCollectorId(collectorId);
    }

    public List<CollectionRequest> getAvailableRequests() {
        return collectionRequestRepository.findByAssignmentStatus(AssignmentStatus.AVAILABLE);
    }

    public List<CollectionRequest> getPendingAssignments() {
        return collectionRequestRepository.findByAssignmentStatus(AssignmentStatus.PENDING);
    }

    public List<CollectionRequest> getRequestsByAssignmentStatus(AssignmentStatus status) {
        return collectionRequestRepository.findByAssignmentStatus(status);
    }

    public List<CollectionRequest> getExpiredAssignments() {
        return collectionRequestRepository.findByAssignmentStatus(AssignmentStatus.EXPIRED);
    }

    public CollectionRequest createRequest(CollectionRequest request, User user) {
        String requestCode = "ECO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        request.setCode(requestCode);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return collectionRequestRepository.save(request);
    }

    public CollectionRequest updateRequest(Long id, CollectionRequest requestDetails) {
        return collectionRequestRepository.findById(id).map(request -> {
            if (requestDetails.getMaterial() != null) {
                request.setMaterial(requestDetails.getMaterial());
            }
            if (requestDetails.getDescription() != null) {
                request.setDescription(requestDetails.getDescription());
            }
            if (requestDetails.getLatitude() != null) {
                request.setLatitude(requestDetails.getLatitude());
            }
            if (requestDetails.getLongitude() != null) {
                request.setLongitude(requestDetails.getLongitude());
            }
            if (requestDetails.getAddress() != null) {
                request.setAddress(requestDetails.getAddress());
            }
            if (requestDetails.getStatus() != null) {
                request.setStatus(requestDetails.getStatus());
            }
            if (requestDetails.getWeight() != null) {
                request.setWeight(requestDetails.getWeight());
            }
            request.setUpdatedAt(LocalDateTime.now());

            return collectionRequestRepository.save(request);
        }).orElse(null);
    }

    public void deleteRequest(Long id) {
        collectionRequestRepository.deleteById(id);
    }

    public CollectionRequest updateAssignmentStatus(Long id, AssignmentStatus status) {
        return collectionRequestRepository.findById(id).map(request -> {
            request.setAssignmentStatus(status);
            request.setUpdatedAt(LocalDateTime.now());
            return collectionRequestRepository.save(request);
        }).orElse(null);
    }

    public List<CollectionRequest> getRequestsByCollectorIdAndStatus(Long collectorId, AssignmentStatus status) {
        return collectionRequestRepository.findByAssignedCollectorIdAndAssignmentStatus(collectorId, status);
    }

    // NUEVOS MÉTODOS PARA MANEJAR DTOs COMPLETOS

    public List<CollectionRequestFullDTO> getAllRequestsWithUserInfo() {
        List<CollectionRequest> requests = collectionRequestRepository.findAllWithUser();
        return convertToFullDTOList(requests);
    }

    public List<CollectionRequestFullDTO> getTodayRequestsWithUserInfo() {
        List<CollectionRequest> requests = collectionRequestRepository.findTodayRequests();
        return convertToFullDTOList(requests);
    }

    public List<CollectionRequestFullDTO> getAvailableRequestsWithUserInfo() {
        List<CollectionRequest> requests = collectionRequestRepository.findAvailableRequestsWithUser();
        return convertToFullDTOList(requests);
    }

    public List<CollectionRequestFullDTO> searchRequestsWithUserInfo(String searchTerm) {
        List<CollectionRequest> requests = collectionRequestRepository.findByCodeOrUserName(searchTerm);
        return convertToFullDTOList(requests);
    }

    private List<CollectionRequestFullDTO> convertToFullDTOList(List<CollectionRequest> requests) {
        return requests.stream()
                .map(this::convertToFullDTO)
                .collect(Collectors.toList());
    }

    public CollectionRequestFullDTO convertToFullDTO(CollectionRequest request) {
        CollectionRequestFullDTO dto = new CollectionRequestFullDTO();
        dto.setId(request.getId());
        dto.setCode(request.getCode());
        dto.setMaterial(request.getMaterial());
        dto.setDescription(request.getDescription());
        dto.setLatitude(request.getLatitude());
        dto.setLongitude(request.getLongitude());
        dto.setAddress(request.getAddress());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        dto.setWeight(request.getWeight());

        // Información del usuario
        if (request.getUser() != null) {
            dto.setUserId(request.getUser().getId());
            dto.setUserName(request.getUser().getName());
            dto.setUserLastname(request.getUser().getLastname());
            dto.setUserEmail(request.getUser().getEmail());
            dto.setUserPhone(request.getUser().getPhone());
        }

        // Información de asignación
        dto.setAssignedCollectorId(request.getAssignedCollectorId());
        dto.setAssignedCollectorName(request.getAssignedCollectorName());
        dto.setAssignedAt(request.getAssignedAt());
        dto.setAssignmentExpiresAt(request.getAssignmentExpiresAt());
        dto.setAssignmentStatus(request.getAssignmentStatus());

        return dto;
    }
}