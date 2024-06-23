package com.example.msaccount.repository.admin;

import com.example.msaccount.entity.admin.AdminRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AdminRoleEntity, Long> {
}
