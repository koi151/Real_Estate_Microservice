package com.example.msaccount.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(name = "user_name", length = 50, nullable = false)
    @NotNull(message = "User name cannot be null")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Username contains invalid characters")
    private String userName;

    @Column(name = "phone", length = 20, nullable = false)
    @NotNull(message = "Phone number cannot be null")
    private String phone;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum accountStatus;

    @Column(name = "first_name", length = 30)
    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @Column(name = "last_name", length = 70)
    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @Column(name = "password", length = 100, nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])",
            message = "Password must include at least one uppercase letter and one special character")
    private String password;

    @Column(name = "email", length = 100, nullable = false)
    @Email(message = "Invalid email")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Column(name = "deleted", length = 20, nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
