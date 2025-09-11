package com.ecocollet.backend.config;

import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.repository.UserRepository;
import com.ecocollet.backend.repository.CollectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;  // ← AGREGAR

    @Autowired
    private CollectionRequestRepository collectionRequestRepository;  // ← AGREGAR

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba si no existen
        createTestUsers();
        createTestRequests();  // ← LLAMAR AL MÉTODO
    }

    private void createTestUsers() {
        // Usuario admin
        if (!userRepository.existsByEmail("admin@ecocollet.com")) {  // ← CAMBIAR
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@ecocollet.com");
            admin.setPassword("admin123");
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);  // ← CAMBIAR
            System.out.println("Usuario admin creado: admin@ecocollet.com / admin123");
        }

        // Recolector de prueba
        if (!userRepository.existsByEmail("recolector@ecocollet.com")) {  // ← CAMBIAR
            User collector = new User();
            collector.setName("Carlos Recolector");
            collector.setEmail("recolector@ecocollet.com");
            collector.setPassword("collector123");
            collector.setRole(Role.ROLE_COLLECTOR);
            userRepository.save(collector);  // ← CAMBIAR
            System.out.println("Usuario recolector creado: recolector@ecocollet.com / collector123");
        }

        // Usuario normal de prueba
        if (!userRepository.existsByEmail("usuario@ecocollet.com")) {  // ← CAMBIAR
            User user = new User();
            user.setName("María Usuario");
            user.setEmail("usuario@ecocollet.com");
            user.setPassword("user123");
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);  // ← CAMBIAR
            System.out.println("Usuario normal creado: usuario@ecocollet.com / user123");
        }
    }

    private void createTestRequests() {
        Optional<User> user = userRepository.findByEmail("usuario@ecocollet.com");
        if (user.isPresent()) {
            // SOLO crear las instancias, el código se genera automáticamente con @PrePersist
            CollectionRequest request1 = new CollectionRequest();
            request1.setUser(user.get());
            request1.setMaterial("Plástico");
            request1.setDescription("Botellas PET");
            request1.setWeight(2.5);
            request1.setStatus(RequestStatus.COLLECTED);
            collectionRequestRepository.save(request1);

            CollectionRequest request2 = new CollectionRequest();
            request2.setUser(user.get());
            request2.setMaterial("Vidrio");
            request2.setDescription("Botellas de vidrio");
            request2.setWeight(3.2);
            request2.setStatus(RequestStatus.COLLECTED);
            collectionRequestRepository.save(request2);

            System.out.println("Solicitudes de prueba creadas para María Usuario");
        }
    }
}