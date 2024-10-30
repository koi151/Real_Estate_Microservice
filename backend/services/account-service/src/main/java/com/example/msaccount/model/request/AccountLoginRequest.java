package com.example.msaccount.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginRequest {

    @NotBlank(message = "Account name cannot be blank")
    private String accountName;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
