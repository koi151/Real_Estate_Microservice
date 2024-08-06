package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.mapper.PostServiceCategoryMapper;
import com.koi151.listing_services.model.dto.PostServiceCategoryCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCategoryCreateRequest;
import com.koi151.listing_services.repository.PostServiceCategoryRepository;
import com.koi151.listing_services.service.PostServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceCategoryServiceImpl implements PostServiceCategoryService {

    private final PostServiceCategoryRepository postServiceCategoryRepository;
    private final PostServiceCategoryMapper postServiceCategoryMapper;

    @Override
    @Transactional
    public PostServiceCategoryCreateDTO createPostServiceCategory(PostServiceCategoryCreateRequest request) {
        var entity = postServiceCategoryRepository.save(
                postServiceCategoryMapper.toPostServiceCategoryEntity(request));
        return postServiceCategoryMapper.toPostServiceCategoryCreateDTO(entity);
    }
}












