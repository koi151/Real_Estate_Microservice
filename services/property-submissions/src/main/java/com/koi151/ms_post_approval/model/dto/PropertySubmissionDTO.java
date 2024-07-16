package com.koi151.ms_post_approval.model.dto;

import com.koi151.ms_post_approval.model.response.PageMeta;
import lombok.Data;

import java.util.List;

@Data
public class PropertySubmissionDTO {
    private List<PropertySubmissionDetailsDTO> content;
    private PageMeta meta;
}
