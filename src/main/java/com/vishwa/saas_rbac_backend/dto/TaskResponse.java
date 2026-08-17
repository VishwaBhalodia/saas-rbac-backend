package com.vishwa.saas_rbac_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String projectName;
    private String createdByName;
    private String assignedToName;
    private LocalDateTime createdAt;
}