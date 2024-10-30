package com.koi151.property_submissions.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertySubmissionDetailedDTO {
    private Long propertyId;
    private String referenceCode;
    private Long reviewerId;
    private String status;
    private String reviewMessage;
}
