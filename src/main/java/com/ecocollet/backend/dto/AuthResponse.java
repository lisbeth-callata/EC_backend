package com.ecocollet.backend.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String username;
    private String phone;
    private String lastname;
    private String role;
    private Long userId;

    // Constructores
    public AuthResponse() {}

    public AuthResponse(String token, String email, String name, String username, String phone, String lastname, String role, Long userId) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.lastname = lastname;
        this.role = role;
        this.userId = userId;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}