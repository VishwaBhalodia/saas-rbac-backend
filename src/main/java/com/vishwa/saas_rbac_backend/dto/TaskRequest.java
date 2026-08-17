package com.vishwa.saas_rbac_backend.dto;

import lombok.Data;

@Data
public class TaskRequest {
    private Long projectId;
    private String title;
    private String description;
    private Long assignedToUserId;
}