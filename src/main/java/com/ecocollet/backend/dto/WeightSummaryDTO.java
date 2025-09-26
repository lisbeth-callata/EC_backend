package com.ecocollet.backend.dto;

public class WeightSummaryDTO {
    private Long userId;
    private String userName;
    private String userLastname;
    private String userEmail;
    private Long totalRequests;
    private Double totalWeight;

    // Constructor que Hibernate necesita (EXACTO para la query)
    public WeightSummaryDTO(Long userId, String userName, String userLastname,
                            String userEmail, Long totalRequests, Double totalWeight) {
        this.userId = userId;
        this.userName = userName;
        this.userLastname = userLastname;
        this.userEmail = userEmail;
        this.totalRequests = totalRequests;
        this.totalWeight = totalWeight;
    }

    // Constructor vacío
    public WeightSummaryDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserLastname() { return userLastname; }
    public void setUserLastname(String userLastname) { this.userLastname = userLastname; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getTotalRequests() { return totalRequests; }  // Cambiado a Long
    public void setTotalRequests(Long totalRequests) { this.totalRequests = totalRequests; }

    public Double getTotalWeight() { return totalWeight; }  // Cambiado a Double
    public void setTotalWeight(Double totalWeight) { this.totalWeight = totalWeight; }
}