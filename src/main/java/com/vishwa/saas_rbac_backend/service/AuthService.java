package com.vishwa.saas_rbac_backend.service;

import com.vishwa.saas_rbac_backend.dto.*;
import com.vishwa.saas_rbac_backend.entity.*;
import com.vishwa.saas_rbac_backend.repository.*;
import com.vishwa.saas_rbac_backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       OrganizationRepository organizationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        Organization org = new Organization();
        org.setName(request.getOrgName());
        org.setSlug(request.getOrgName().toLowerCase().replace(" ", "-"));
        organizationRepository.save(org);

        User user = new User();
        user.setOrganization(org);
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), org.getId(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), org.getId());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getOrganization().getId(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getOrganization().getId());
    }
}