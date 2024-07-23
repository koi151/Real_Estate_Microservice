package com.koi151.property_submissions.model.dto;

import com.koi151.property_submissions.model.response.PageMeta;
import lombok.Data;

import java.util.List;

@Data
public class PropertySubmissionsDetailsDTO {
    private List<PropertySubmissionDetailedDTO> content;
    private PageMeta meta;
}
