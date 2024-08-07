package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.customExceptions.DuplicatePostServiceException;
import com.koi151.listing_services.customExceptions.PostServiceCategoryNotFoundException;
import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.mapper.ListingServiceMapper;
import com.koi151.listing_services.model.dto.PostServiceCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import com.koi151.listing_services.repository.PostServiceCategoryRepository;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.service.ListingServicesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListingServicesServiceImpl implements ListingServicesService {

    private final ListingServiceMapper listingServiceMapper;
    private final PostServiceRepository postServiceRepository;
    private final PostServiceCategoryRepository postServiceCategoryRepository;

    @Override
    @Transactional
    public PostServiceCreateDTO createPostService(PostServiceCreateRequest request) {
        if (!postServiceCategoryRepository.existsById(Math.toIntExact(request.postServiceCategoryId()))) // category check
            throw new PostServiceCategoryNotFoundException("Cannot found post service category with id: " + request.postServiceCategoryId());
        if (postServiceRepository.existsByName(request.name()))
            throw new DuplicatePostServiceException("Post service with name '" + request.name() + "' already existed");

        PostService entity = postServiceRepository.save(listingServiceMapper.toPostServiceEntity(request));
        return listingServiceMapper.toPostServiceCreateDTO(entity);
    }
}






