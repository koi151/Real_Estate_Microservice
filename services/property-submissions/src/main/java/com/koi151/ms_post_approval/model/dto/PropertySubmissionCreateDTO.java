package com.koi151.ms_post_approval.model.dto;

import com.koi151.ms_post_approval.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PropertySubmissionCreateDTO {
    private Long propertyId;
    private Long clientId;
    private String referenceCode;
    private Long reviewerId;
    private PostStatus status;
    private String reviewMessage;
}
