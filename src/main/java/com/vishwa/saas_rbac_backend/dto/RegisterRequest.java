package com.vishwa.saas_rbac_backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String orgName;
    private String fullName;
    private String email;
    private String password;
}
