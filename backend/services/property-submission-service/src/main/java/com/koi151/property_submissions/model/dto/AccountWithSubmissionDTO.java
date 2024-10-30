package com.koi151.property_submissions.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountWithSubmissionDTO {
    private AccountWithNameAndRoleDTO accountWithNameAndRoleDTO;
    private PropertySubmissionsDetailsDTO propertySubmissionsDetailsDTO;
}