package com.vishwa.saas_rbac_backend.util;

public class TenantContext {

    private static final ThreadLocal<Long> currentOrgId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentRole = new ThreadLocal<>();
    private static final ThreadLocal<String> currentEmail = new ThreadLocal<>();

    public static void setOrgId(Long orgId) {
        currentOrgId.set(orgId);
    }

    public static Long getOrgId() {
        return currentOrgId.get();
    }

    public static void setRole(String role) {
        currentRole.set(role);
    }

    public static String getRole() {
        return currentRole.get();
    }

    public static void setEmail(String email) {
        currentEmail.set(email);
    }

    public static String getEmail() {
        return currentEmail.get();
    }

    public static void clear() {
        currentOrgId.remove();
        currentRole.remove();
        currentEmail.remove();
    }
}