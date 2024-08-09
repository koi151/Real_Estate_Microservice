package com.koi151.listing_services.service;

import com.koi151.listing_services.model.dto.PostServiceCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;

public interface ListingServicesService {

    PostServiceCreateDTO createPostService(PostServiceCreateRequest request);
}
