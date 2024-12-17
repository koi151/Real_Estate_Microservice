package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.customExceptions.DuplicatePostServiceException;
import com.koi151.listing_services.customExceptions.EntityNotFoundCustomException;
import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.enums.Status;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.model.dto.PostServiceDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import com.koi151.listing_services.repository.PostServiceCategoryRepository;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.service.PostServiceService;
import com.koi151.listing_services.service.converter.PostServiceConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceServiceImpl implements PostServiceService {

    private final PostServiceCategoryRepository postServiceCategoryRepository;
    private final PostServiceRepository postServiceRepository;
    private final PostServiceMapper postServiceMapper;
    private final PostServiceConverter postServiceConverter;

    @Override
    public List<PostServiceDTO> findActiveServices() {
        List<PostService> postServices = postServiceRepository.findAllByStatus(Status.ACTIVE);
        return postServiceMapper.toPostServiceDTOList(postServices);
    }

    @Override
    @Transactional
    public PostServiceDTO createPostService(PostServiceCreateRequest request) {
        if (!postServiceCategoryRepository.existsById(Math.toIntExact(request.postServiceCategoryId()))) // category check
            throw new EntityNotFoundCustomException("Cannot found post service category with id: " + request.postServiceCategoryId());
        if (postServiceRepository.existsByName(request.name()))
            throw new DuplicatePostServiceException("Post service with name '" + request.name() + "' already existed");

        PostService entity = postServiceConverter.toPostServiceEntity(request);
        PostService savedData = postServiceRepository.save(entity);
        return postServiceMapper.toPostServiceDTO(savedData);
    }
}
