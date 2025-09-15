package com.ecocollet.backend.dto;

public class RegisterResponse {
    private String message;
    private Long userId;

    public RegisterResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }

    // Getters y setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}