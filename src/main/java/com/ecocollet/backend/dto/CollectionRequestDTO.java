package com.ecocollet.backend.dto;

import com.ecocollet.backend.model.RequestStatus;

public class CollectionRequestDTO {
    private Long id;
    private String code;
    private String userEmail;
    private String userName;
    private String material;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private RequestStatus status;
    private String createdAt;
    private String updatedAt;
    private Double weight;

    // Constructores
    public CollectionRequestDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}