package com.ecocollet.backend.config;

import com.ecocollet.backend.model.CollectionRequest;
import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.repository.UserRepository;
import com.ecocollet.backend.repository.CollectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRequestRepository collectionRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba si no existen
        createTestUsers();
        createTestRequests();
    }

    private void createTestUsers() {
        // Usuario admin
        if (!userRepository.existsByEmail("admin@ecocollet.com")) {
            User admin = new User(
                    "Administrador",
                    "Sistema",
                    "admin",
                    "admin@ecocollet.com",
                    "123456789",
                    "admin123",
                    Role.ROLE_ADMIN
            );
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            userRepository.save(admin);
            System.out.println("Usuario admin creado: admin@ecocollet.com / admin123");
        }

        // Recolector de prueba
        if (!userRepository.existsByEmail("recolector@ecocollet.com")) {
            User collector = new User(
                    "Carlos",
                    "Recolector",
                    "carlos_col",
                    "recolector@ecocollet.com",
                    "987654321",
                    "collector123",
                    Role.ROLE_COLLECTOR
            );
            collector.setPassword(passwordEncoder.encode(collector.getPassword()));
            userRepository.save(collector);
            System.out.println("Usuario recolector creado: recolector@ecocollet.com / collector123");
        }

        // Usuario normal de prueba
        if (!userRepository.existsByEmail("usuario@ecocollet.com")) {
            User user = new User(
                    "María",
                    "Usuario",
                    "maria_user",
                    "usuario@ecocollet.com",
                    "555555555",
                    "user123",
                    Role.ROLE_USER
            );
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            System.out.println("Usuario normal creado: usuario@ecocollet.com / user123");
        }

        // Verificar si existen otros usuarios de prueba y actualizarlos si es necesario
        updateExistingTestUsers();
    }

    private void updateExistingTestUsers() {
        // Actualizar usuarios existentes que puedan tener campos null
        Optional<User> existingAdmin = userRepository.findByEmail("admin@ecocollet.com");
        existingAdmin.ifPresent(user -> {
            if (user.getLastname() == null) user.setLastname("Sistema");
            if (user.getUsername() == null) user.setUsername("admin");
            if (user.getPhone() == null) user.setPhone("123456789");
            userRepository.save(user);
        });

        Optional<User> existingCollector = userRepository.findByEmail("recolector@ecocollet.com");
        existingCollector.ifPresent(user -> {
            if (user.getLastname() == null) user.setLastname("Recolector");
            if (user.getUsername() == null) user.setUsername("carlos_col");
            if (user.getPhone() == null) user.setPhone("987654321");
            userRepository.save(user);
        });

        Optional<User> existingUser = userRepository.findByEmail("usuario@ecocollet.com");
        existingUser.ifPresent(user -> {
            if (user.getLastname() == null) user.setLastname("Usuario");
            if (user.getUsername() == null) user.setUsername("maria_user");
            if (user.getPhone() == null) user.setPhone("555555555");
            userRepository.save(user);
        });
    }

    private void createTestRequests() {
        Optional<User> user = userRepository.findByEmail("usuario@ecocollet.com");
        if (user.isPresent()) {
            // Crear solicitudes de prueba
            CollectionRequest request1 = new CollectionRequest();
            request1.setUser(user.get());
            request1.setMaterial("Plástico");
            request1.setDescription("Botellas PET");
            request1.setLatitude(-12.0464);
            request1.setLongitude(-77.0428);
            request1.setAddress("Av. Lima 123, Lima");
            request1.setWeight(2.5);
            request1.setStatus(RequestStatus.COLLECTED);
            collectionRequestRepository.save(request1);

            CollectionRequest request2 = new CollectionRequest();
            request2.setUser(user.get());
            request2.setMaterial("Vidrio");
            request2.setDescription("Botellas de vidrio");
            request2.setLatitude(-12.0564);
            request2.setLongitude(-77.0328);
            request2.setAddress("Av. Arequipa 456, Lima");
            request2.setWeight(3.2);
            request2.setStatus(RequestStatus.COLLECTED);
            collectionRequestRepository.save(request2);

            // Solicitud pendiente
            CollectionRequest request3 = new CollectionRequest();
            request3.setUser(user.get());
            request3.setMaterial("Papel");
            request3.setDescription("Periódicos y revistas");
            request3.setLatitude(-12.0664);
            request3.setLongitude(-77.0228);
            request3.setAddress("Jr. Callao 789, Lima");
            request3.setStatus(RequestStatus.PENDING);
            collectionRequestRepository.save(request3);

            System.out.println("3 solicitudes de prueba creadas para María Usuario");
        }
    }
}