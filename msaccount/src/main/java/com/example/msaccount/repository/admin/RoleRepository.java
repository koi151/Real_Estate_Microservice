package com.example.msaccount.repository.admin;

import com.example.msaccount.entity.admin.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AdminRole, Long> {
}
