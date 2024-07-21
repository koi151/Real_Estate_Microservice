package com.koi151.ms_post_approval.model.dto;

import com.koi151.ms_post_approval.model.response.PageMeta;
import lombok.Data;

import java.util.List;

@Data
public class PropertySubmissionsDetailsDTO {
    private List<PropertySubmissionDetailedDTO> content;
    private PageMeta meta;
}
