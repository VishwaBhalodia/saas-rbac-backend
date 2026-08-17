package com.vishwa.saas_rbac_backend.repository;

import com.vishwa.saas_rbac_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOrganizationId(Long orgId);
    List<Task> findByProjectIdAndOrganizationId(Long projectId, Long orgId);
    Optional<Task> findByIdAndOrganizationId(Long id, Long orgId);
}