package com.koi151.ms_post_approval.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountWithSubmissionDTO {
    private AccountWithNameAndRoleDTO accountWithNameAndRoleDTO;
    private PropertySubmissionDTO propertySubmissionDTO;
}