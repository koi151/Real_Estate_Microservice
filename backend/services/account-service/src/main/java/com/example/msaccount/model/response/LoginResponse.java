package com.example.msaccount.model.response;

import lombok.*;
import org.keycloak.representations.AccessTokenResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UserInfoResponse userInfo;

}
