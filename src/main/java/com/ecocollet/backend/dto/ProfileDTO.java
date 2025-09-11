package com.ecocollet.backend.dto;

public class ProfileDTO {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private int totalRequests;
    private double totalWeight;

    // Constructores
    public ProfileDTO() {}

    public ProfileDTO(Long userId, String name, String email, String role, int totalRequests, double totalWeight) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.totalRequests = totalRequests;
        this.totalWeight = totalWeight;
    }

    // Getters y Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }

    public double getTotalWeight() { return totalWeight; }
    public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
}