package com.vishwa.saas_rbac_backend.repository;

import com.vishwa.saas_rbac_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOrganizationId(Long orgId);
    Optional<Project> findByIdAndOrganizationId(Long id, Long orgId);
}