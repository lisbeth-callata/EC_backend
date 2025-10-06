package com.ecocollet.backend.config;

import com.ecocollet.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://192.168.*:*",
                "http://10.*:*",
                "https://*.render.com",
                "https://*.railway.app",
                "https://*.googleapis.com",
                "http://127.0.0.1:*",
                "https://*.vercel.app",
                "https://ec-frontend-livid.vercel.app",
                "https://ec-frontend-*.vercel.app",
                "https://*.netlify.app",
                "https://ecocollet.netlify.app",
                "https://ecocollet-admin.netlify.app"


        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/requests/**").hasAnyRole("USER", "COLLECTOR", "ADMIN")
                        .requestMatchers("/api/requests/{id}").hasAnyRole("USER", "COLLECTOR", "ADMIN")
                        .requestMatchers("/api/requests/{id}/update").hasAnyRole("COLLECTOR", "ADMIN")
                        .requestMatchers("/api/collector/requests/**").hasAnyRole("COLLECTOR", "ADMIN")
                        .requestMatchers("/api/assignments/**").hasAnyRole("COLLECTOR", "ADMIN")
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/users/{userId}/profile").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/me/profile").hasAnyRole("USER", "ADMIN", "COLLECTOR")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/collector/**").hasAnyRole("COLLECTOR", "ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "COLLECTOR", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}