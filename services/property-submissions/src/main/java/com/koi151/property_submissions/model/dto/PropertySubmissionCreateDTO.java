package com.koi151.property_submissions.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PropertySubmissionCreateDTO {
    private Long propertyId;
    private Long accountId;
    private String referenceCode;
    private Long reviewerId;
    private String status;
    private String reviewMessage;
}
