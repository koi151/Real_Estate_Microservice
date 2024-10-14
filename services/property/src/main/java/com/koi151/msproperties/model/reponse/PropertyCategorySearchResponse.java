package com.koi151.msproperties.model.reponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategorySearchResponse {

    private int categoryId;
    private String title;
    private String description;
    private String status;
    private boolean deleted;
}
