package com.ecocollet.backend.dto;

import com.ecocollet.backend.model.RequestStatus;
import com.ecocollet.backend.model.AssignmentStatus;
import java.time.LocalDateTime;

public class CollectionRequestFullDTO {
    private Long id;
    private String code;
    private String material;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private String district;
    private String province;
    private String region;
    private String addressUser;
    private String reference;

    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double weight;

    // Información del usuario
    private Long userId;
    private String userName;
    private String userLastname;
    private String userEmail;
    private String userPhone;

    // Información de asignación
    private Long assignedCollectorId;
    private String assignedCollectorName;
    private LocalDateTime assignedAt;
    private LocalDateTime assignmentExpiresAt;
    private AssignmentStatus assignmentStatus;

    // Constructores
    public CollectionRequestFullDTO() {}

    public CollectionRequestFullDTO(Long id, String code, String material, String description,
                                    Double latitude, Double longitude, String address, String district, String province, String region,
                                    String addressUser, String reference,
                                    RequestStatus status, LocalDateTime createdAt,
                                    LocalDateTime updatedAt, Double weight, Long userId,
                                    String userName, String userLastname, String userEmail,
                                    String userPhone, Long assignedCollectorId,
                                    String assignedCollectorName, LocalDateTime assignedAt,
                                    LocalDateTime assignmentExpiresAt, AssignmentStatus assignmentStatus) {
        this.id = id;
        this.code = code;
        this.material = material;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.district = district;
        this.province = province;
        this.region = region;
        this.addressUser = addressUser;
        this.reference = reference;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.weight = weight;
        this.userId = userId;
        this.userName = userName;
        this.userLastname = userLastname;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.assignedCollectorId = assignedCollectorId;
        this.assignedCollectorName = assignedCollectorName;
        this.assignedAt = assignedAt;
        this.assignmentExpiresAt = assignmentExpiresAt;
        this.assignmentStatus = assignmentStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

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

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getAddressUser() { return addressUser; }
    public void setAddressUser(String addressUser) { this.addressUser = addressUser; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserLastname() { return userLastname; }
    public void setUserLastname(String userLastname) { this.userLastname = userLastname; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public Long getAssignedCollectorId() { return assignedCollectorId; }
    public void setAssignedCollectorId(Long assignedCollectorId) { this.assignedCollectorId = assignedCollectorId; }

    public String getAssignedCollectorName() { return assignedCollectorName; }
    public void setAssignedCollectorName(String assignedCollectorName) { this.assignedCollectorName = assignedCollectorName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getAssignmentExpiresAt() { return assignmentExpiresAt; }
    public void setAssignmentExpiresAt(LocalDateTime assignmentExpiresAt) { this.assignmentExpiresAt = assignmentExpiresAt; }

    public AssignmentStatus getAssignmentStatus() { return assignmentStatus; }
    public void setAssignmentStatus(AssignmentStatus assignmentStatus) { this.assignmentStatus = assignmentStatus; }
}