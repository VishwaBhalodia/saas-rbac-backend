package com.vishwa.saas_rbac_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private Long orgId;
}