package com.vishwa.saas_rbac_backend.service;

import com.vishwa.saas_rbac_backend.dto.*;
import com.vishwa.saas_rbac_backend.entity.*;
import com.vishwa.saas_rbac_backend.repository.*;
import com.vishwa.saas_rbac_backend.util.TenantContext;
import org.springframework.stereotype.Service;
import com.vishwa.saas_rbac_backend.exception.ResourceNotFoundException;
import com.vishwa.saas_rbac_backend.exception.AccessDeniedException;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse create(ProjectRequest request) {
        Long orgId = TenantContext.getOrgId();
        String email = TenantContext.getEmail();

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOrganization(creator.getOrganization());
        project.setCreatedBy(creator);

        projectRepository.save(project);

        return toResponse(project);
    }

    public List<ProjectResponse> listAll() {
        Long orgId = TenantContext.getOrgId();
        return projectRepository.findByOrganizationId(orgId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse getOne(Long id) {
        Long orgId = TenantContext.getOrgId();
        Project project = projectRepository.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return toResponse(project);
    }

    public void delete(Long id) {
        Long orgId = TenantContext.getOrgId();
        String role = TenantContext.getRole();

        if (!role.equals("ADMIN")) {
            throw new AccessDeniedException("Only admins can delete projects");
        }

        Project project = projectRepository.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedBy().getFullName(),
                project.getCreatedAt()
        );
    }
}