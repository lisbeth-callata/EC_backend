package com.ecocollet.backend.repository;

import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRequestRepository extends JpaRepository<CollectionRequest, Long> {
    Optional<CollectionRequest> findByCode(String code);

    @Query("SELECT cr FROM CollectionRequest cr JOIN FETCH cr.user WHERE cr.user = :user")
    List<CollectionRequest> findByUser(@Param("user") User user);

    List<CollectionRequest> findByStatus(RequestStatus status);
    List<CollectionRequest> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CollectionRequest> findByAssignedCollectorId(Long collectorId);
    List<CollectionRequest> findByAssignmentStatus(AssignmentStatus status);

    List<CollectionRequest> findByAssignmentStatusAndAssignmentExpiresAtBefore(
            AssignmentStatus status, LocalDateTime expiryTime);

    List<CollectionRequest> findByAssignmentStatusAndAssignedAtBefore(
            AssignmentStatus status, LocalDateTime assignedTime);

    List<CollectionRequest> findByAssignedCollectorIdAndAssignmentStatus(
            Long collectorId, AssignmentStatus status);

    @Query("SELECT cr FROM CollectionRequest cr JOIN FETCH cr.user WHERE DATE(cr.createdAt) = CURRENT_DATE")
    List<CollectionRequest> findTodayRequests();

    @Query("SELECT cr FROM CollectionRequest cr JOIN FETCH cr.user WHERE cr.code LIKE %:searchTerm% OR cr.user.name LIKE %:searchTerm% OR cr.user.lastname LIKE %:searchTerm%")
    List<CollectionRequest> findByCodeOrUserName(@Param("searchTerm") String searchTerm);

    @Query("SELECT COUNT(cr) FROM CollectionRequest cr WHERE cr.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(cr.weight) FROM CollectionRequest cr WHERE cr.user.id = :userId AND cr.weight IS NOT NULL")
    Double sumWeightByUserId(@Param("userId") Long userId);

    @Query("SELECT cr FROM CollectionRequest cr JOIN FETCH cr.user WHERE cr.assignmentStatus = com.ecocollet.backend.model.AssignmentStatus.AVAILABLE")
    List<CollectionRequest> findAvailableRequestsWithUser();

    @Query("SELECT cr FROM CollectionRequest cr JOIN FETCH cr.user")
    List<CollectionRequest> findAllWithUser();
}