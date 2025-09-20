package com.ecocollet.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "collection_requests")
public class CollectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "code", unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @NotBlank
    @Size(max = 100)
    @Column(name = "material")
    private String material;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Size(max = 500)
    @Column(name = "address")
    private String address;

    // CAMBIO IMPORTANTE: Usar EnumType en lugar de String
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "weight")
    private Double weight;

    // Constructores
    public CollectionRequest() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }

    public CollectionRequest(String code, User user, String material, String description) {
        this();
        this.code = code;
        this.user = user;
        this.material = material;
        this.description = description;
    }



    // Getters y Setters (mantener todos los existentes)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    // CAMBIO: Getter y Setter para RequestStatus (enum)
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "ECO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}