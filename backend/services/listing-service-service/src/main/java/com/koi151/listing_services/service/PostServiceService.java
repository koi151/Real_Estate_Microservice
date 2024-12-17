package com.koi151.listing_services.service;

import com.koi151.listing_services.model.dto.PostServiceDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;

import java.util.List;

public interface PostServiceService {
    List<PostServiceDTO> findActiveServices();
    PostServiceDTO createPostService(PostServiceCreateRequest request);

}
