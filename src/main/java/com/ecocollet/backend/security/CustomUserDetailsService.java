package com.ecocollet.backend.security;

import com.ecocollet.backend.model.User;
import com.ecocollet.backend.model.Role; // ¡Asegúrate de importar Role!
import com.ecocollet.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user.getRole()) // Ahora pasa el enum directamente
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        // Usa role.name() que devuelve "ROLE_USER", "ROLE_ADMIN", etc.
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}