package com.example.msaccount.entity;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.admin.Permission;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity(name = "roles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Account> accounts;

    @ManyToMany
    @JoinTable(
            name="role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissionsEntities;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 100, message = "Role name cannot exceed 100 characters")
    private String name;

    private String description;

    private boolean deleted = false;
}

