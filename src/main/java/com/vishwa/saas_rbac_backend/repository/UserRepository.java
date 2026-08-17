package com.vishwa.saas_rbac_backend.repository;

import com.vishwa.saas_rbac_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}