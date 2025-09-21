package com.ecocollet.backend.dto;

public class AssignmentRequestDTO {
    private Long collectorId;
    private String collectorName;
    private Integer timeoutMinutes; // Tiempo para completar la asignación

    // Constructores
    public AssignmentRequestDTO() {}

    public AssignmentRequestDTO(Long collectorId, String collectorName, Integer timeoutMinutes) {
        this.collectorId = collectorId;
        this.collectorName = collectorName;
        this.timeoutMinutes = timeoutMinutes;
    }

    // Getters y Setters
    public Long getCollectorId() { return collectorId; }
    public void setCollectorId(Long collectorId) { this.collectorId = collectorId; }

    public String getCollectorName() { return collectorName; }
    public void setCollectorName(String collectorName) { this.collectorName = collectorName; }

    public Integer getTimeoutMinutes() { return timeoutMinutes; }
    public void setTimeoutMinutes(Integer timeoutMinutes) { this.timeoutMinutes = timeoutMinutes; }
}