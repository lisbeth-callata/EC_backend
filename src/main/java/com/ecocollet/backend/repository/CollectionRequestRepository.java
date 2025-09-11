package com.ecocollet.backend.repository;

import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.User;
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
    List<CollectionRequest> findByUser(User user);
    List<CollectionRequest> findByStatus(RequestStatus status);
    List<CollectionRequest> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Solicitudes del día actual para el recolector
    @Query("SELECT cr FROM CollectionRequest cr WHERE DATE(cr.createdAt) = CURRENT_DATE")
    List<CollectionRequest> findTodayRequests();

    // Buscar por código o nombre de usuario
    @Query("SELECT cr FROM CollectionRequest cr WHERE cr.code LIKE %:searchTerm% OR cr.user.name LIKE %:searchTerm%")
    List<CollectionRequest> findByCodeOrUserName(@Param("searchTerm") String searchTerm);

    @Query("SELECT COUNT(cr) FROM CollectionRequest cr WHERE cr.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(cr.weight) FROM CollectionRequest cr WHERE cr.user.id = :userId AND cr.weight IS NOT NULL")
    Double sumWeightByUserId(@Param("userId") Long userId);
}