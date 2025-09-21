package com.ecocollet.backend.dto;

public class CollectorUpdateRequestDTO {
    private Double weight;
    private String status;
    private String notes;

    // Constructores
    public CollectorUpdateRequestDTO() {}

    public CollectorUpdateRequestDTO(Double weight, String status, String notes) {
        this.weight = weight;
        this.status = status;
        this.notes = notes;
    }

    // Getters y Setters
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}