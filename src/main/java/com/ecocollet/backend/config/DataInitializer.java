package com.ecocollet.backend.config;

import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role;
import com.ecocollet.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba si no existen
        createTestUsers();
    }

    private void createTestUsers() {
        // Usuario admin
        if (!userService.existsByEmail("admin@ecocollet.com")) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@ecocollet.com");
            admin.setPassword("admin123");
            admin.setRole(Role.ROLE_ADMIN);
            userService.createUser(admin);
            System.out.println("Usuario admin creado: admin@ecocollet.com / admin123");
        }

        // Recolector de prueba
        if (!userService.existsByEmail("recolector@ecocollet.com")) {
            User collector = new User();
            collector.setName("Carlos Recolector");
            collector.setEmail("recolector@ecocollet.com");
            collector.setPassword("collector123");
            collector.setRole(Role.ROLE_COLLECTOR);
            userService.createUser(collector);
            System.out.println("Usuario recolector creado: recolector@ecocollet.com / collector123");
        }

        // Usuario normal de prueba
        if (!userService.existsByEmail("usuario@ecocollet.com")) {
            User user = new User();
            user.setName("María Usuario");
            user.setEmail("usuario@ecocollet.com");
            user.setPassword("user123");
            user.setRole(Role.ROLE_USER);
            userService.createUser(user);
            System.out.println("Usuario normal creado: usuario@ecocollet.com / user123");
        }
    }
}