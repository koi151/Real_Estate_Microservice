package com.example.msaccount.entity.admin;

import com.example.msaccount.enums.admin.PermissionEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity(name = "permissions") // by default, table have the same name as entity name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    @NotBlank(message = "Permission name cannot be blank") // check
    private PermissionEnum name;
}
