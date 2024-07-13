package com.example.msaccount.entity;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.client.ClientAccount;
import com.example.msaccount.enums.AccountStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account extends BaseEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = -896035267760632084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private AdminAccount adminAccount;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private ClientAccount clientAccount;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "account_name", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Account name cannot be blank")
    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Account name contains invalid characters")
    private String accountName;

    @Column(name = "phone", length = 20, nullable = false)
    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum accountStatus = AccountStatusEnum.ACTIVE;

    @Column(name = "password", length = 100, nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
            message = "Password must include at least one uppercase letter and one special character")
    private String password;

    @Column(name = "first_name", length = 30)
    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @Column(name = "last_name", length = 70)
    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @Email(message = "Invalid email")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
////        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
//        return authorityList;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        role.getPermissionsEntities().forEach(permission ->
                authorityList.add(new SimpleGrantedAuthority(permission.getName().name())));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return accountName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

