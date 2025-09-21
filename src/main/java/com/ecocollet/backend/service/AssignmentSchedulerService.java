package com.ecocollet.backend.service;

import com.ecocollet.backend.model.AssignmentStatus;
import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.repository.CollectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentSchedulerService {

    @Autowired
    private CollectionRequestRepository collectionRequestRepository;

    // Verificar asignaciones expiradas cada 5 minutos
    @Scheduled(fixedRate = 300000) // 5 minutos
    public void checkExpiredAssignments() {
        List<CollectionRequest> expiredAssignments = collectionRequestRepository
                .findByAssignmentStatusAndAssignmentExpiresAtBefore(
                        AssignmentStatus.PENDING,
                        LocalDateTime.now()
                );

        for (CollectionRequest request : expiredAssignments) {
            request.setAssignmentStatus(AssignmentStatus.EXPIRED);
            request.setAssignedCollectorId(null);
            request.setAssignedCollectorName(null);
            request.setAssignmentExpiresAt(null);
            collectionRequestRepository.save(request);
        }
    }

    // Limpiar asignaciones completadas antiguas cada día a medianoche
    @Scheduled(cron = "0 0 0 * * ?") // Cada día a medianoche
    public void cleanupOldAssignments() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<CollectionRequest> oldAssignments = collectionRequestRepository
                .findByAssignmentStatusAndAssignedAtBefore(
                        AssignmentStatus.COMPLETED,
                        thirtyDaysAgo
                );

        // Aquí puedes implementar lógica de archivo o limpieza
    }
}