//package com.koi151.msproperties.service.impl;
//
//import com.koi151.msproperties.model.dto.PropertyPostServiceDTO;
//import com.koi151.msproperties.service.PropertyPostService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PropertyPostServiceImpl implements PropertyPostService {
//
//    private final PropertyPostServiceRepository propertyPostServiceRepository;
//    private final PropertyPostServiceMapper propertyPostServiceMapper;
//
//    @Override
//    @Transactional
//    public PropertyPostServiceDTO findPostServicesById(Long propertyId) {
//        return propertyPostServiceMapper.toPropertyPostServiceDTO(
//                propertyPostServiceRepository.findByPropertyPropertyIdAndPropertyDeleted(propertyId, false));
//    }
//}

