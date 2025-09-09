package com.ecocollet.backend.repository;

import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRequestsIsNotEmpty(); // Usuarios con al menos una solicitud
    List<User> findByRequestsIsEmpty();    // Usuarios sin solicitudes
}