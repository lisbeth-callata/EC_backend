package com.ecocollet.backend.repository;

import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    // En UserRepository.java - EL MÉTODO DEBE RECIBIR SOLO 1 PARÁMETRO
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.username = :identifier")
    Optional<User> findByEmailOrUsername(@Param("identifier") String identifier);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findByRole(Role role);
    List<User> findByRequestsIsNotEmpty();
    List<User> findByRequestsIsEmpty();

    // Búsqueda de usuarios para admin
    @Query("SELECT u FROM User u WHERE u.name LIKE %:search% OR u.lastname LIKE %:search% OR u.email LIKE %:search% OR u.username LIKE %:search% OR u.phone LIKE %:search%")
    List<User> searchUsers(@Param("search") String search);

    @Query("SELECT COUNT(r) FROM CollectionRequest r WHERE r.user.id = :userId")
    int countRequestsByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(r.weight), 0) FROM CollectionRequest r WHERE r.user.id = :userId AND r.weight IS NOT NULL")
    double sumWeightByUserId(@Param("userId") Long userId);
}