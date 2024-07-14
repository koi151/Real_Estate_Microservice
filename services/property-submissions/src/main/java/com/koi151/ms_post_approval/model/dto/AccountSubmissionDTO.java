package com.koi151.ms_post_approval.model.dto;

import com.koi151.ms_post_approval.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSubmissionDTO {
    private int accountId;
    private String accountName;
    private String role;
    private Long propertyId;
    private String referenceCode;
    private Long reviewerId;
    private PostStatus status;
    private String reviewMessage;
}
