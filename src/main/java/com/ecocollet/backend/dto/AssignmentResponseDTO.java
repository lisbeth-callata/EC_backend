package com.ecocollet.backend.dto;

import com.ecocollet.backend.model.AssignmentStatus;
import java.time.LocalDateTime;

public class AssignmentResponseDTO {
    private Long requestId;
    private String requestCode;
    private Long assignedCollectorId;
    private String assignedCollectorName;
    private AssignmentStatus assignmentStatus;
    private LocalDateTime assignedAt;
    private LocalDateTime expiresAt;
    private String message;

    // Constructores
    public AssignmentResponseDTO() {}

    public AssignmentResponseDTO(Long requestId, String requestCode, Long assignedCollectorId,
                                 String assignedCollectorName, AssignmentStatus assignmentStatus,
                                 LocalDateTime assignedAt, LocalDateTime expiresAt, String message) {
        this.requestId = requestId;
        this.requestCode = requestCode;
        this.assignedCollectorId = assignedCollectorId;
        this.assignedCollectorName = assignedCollectorName;
        this.assignmentStatus = assignmentStatus;
        this.assignedAt = assignedAt;
        this.expiresAt = expiresAt;
        this.message = message;
    }

    // Getters y Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getRequestCode() { return requestCode; }
    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }

    public Long getAssignedCollectorId() { return assignedCollectorId; }
    public void setAssignedCollectorId(Long assignedCollectorId) { this.assignedCollectorId = assignedCollectorId; }

    public String getAssignedCollectorName() { return assignedCollectorName; }
    public void setAssignedCollectorName(String assignedCollectorName) { this.assignedCollectorName = assignedCollectorName; }

    public AssignmentStatus getAssignmentStatus() { return assignmentStatus; }
    public void setAssignmentStatus(AssignmentStatus assignmentStatus) { this.assignmentStatus = assignmentStatus; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}