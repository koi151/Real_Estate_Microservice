package com.koi151.ms_post_approval.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertySubmissionDetailsDTO {
    private Long propertyId;
    private String referenceCode;
    private Long reviewerId;
    private String status;
    private String reviewMessage;
}
