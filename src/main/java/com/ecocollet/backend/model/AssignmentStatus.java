package com.ecocollet.backend.model;

public enum AssignmentStatus {
    AVAILABLE,      // Disponible para cualquier recolector
    PENDING,        // Recolector en camino
    IN_PROGRESS,    // Recolector en el lugar
    COMPLETED,      // Recolección completada
    CANCELLED,      // Recolección cancelada
    EXPIRED         // Asignación expirada
}