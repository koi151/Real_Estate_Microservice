package com.example.msaccount.entity.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "permissions") // by default, table have the same name as entity name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(name = "name")
    @NotBlank(message = "Permission name cannot be blank")
    @Size(min = 4, max = 100, message = "Permission name must between {min} and {max} characters")
    private String name;
}
