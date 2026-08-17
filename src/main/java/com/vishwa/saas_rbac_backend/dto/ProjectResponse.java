package com.vishwa.saas_rbac_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String createdByName;
    private LocalDateTime createdAt;
}