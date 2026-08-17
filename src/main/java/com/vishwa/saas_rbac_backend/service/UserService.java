package com.vishwa.saas_rbac_backend.service;

import com.vishwa.saas_rbac_backend.dto.AddMemberRequest;
import com.vishwa.saas_rbac_backend.entity.*;
import com.vishwa.saas_rbac_backend.exception.AccessDeniedException;
import com.vishwa.saas_rbac_backend.exception.ResourceNotFoundException;
import com.vishwa.saas_rbac_backend.repository.UserRepository;
import com.vishwa.saas_rbac_backend.util.TenantContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addMember(AddMemberRequest request) {
        String role = TenantContext.getRole();
        if (!role.equals("ADMIN")) {
            throw new AccessDeniedException("Only admins can add members");
        }

        String adminEmail = TenantContext.getEmail();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        User member = new User();
        member.setOrganization(admin.getOrganization());
        member.setEmail(request.getEmail());
        member.setFullName(request.getFullName());
        member.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        member.setRole(Role.MEMBER);

        userRepository.save(member);
    }
}