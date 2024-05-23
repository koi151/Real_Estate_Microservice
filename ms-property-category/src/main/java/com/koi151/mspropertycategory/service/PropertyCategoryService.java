package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyCategoryService {

    @Autowired
    PropertyCategoryRepository propertyCategoryRepository;

}
