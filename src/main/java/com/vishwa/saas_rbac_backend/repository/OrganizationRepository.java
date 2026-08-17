package com.vishwa.saas_rbac_backend.repository;

import com.vishwa.saas_rbac_backend.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}