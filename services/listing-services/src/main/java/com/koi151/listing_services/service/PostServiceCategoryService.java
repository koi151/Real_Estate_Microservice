package com.koi151.listing_services.service;

import com.koi151.listing_services.model.dto.PostServiceCategoryCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCategoryCreateRequest;

public interface PostServiceCategoryService {
    PostServiceCategoryCreateDTO createPostServiceCategory(PostServiceCategoryCreateRequest request);
}
