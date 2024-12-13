//package com.koi151.msproperty.controller.client;
//
//import com.koi151.msproperty.mapper.ResponseDataMapper;
//import com.koi151.msproperty.model.request.property.PropertySearchRequest;
//import com.koi151.msproperty.model.response.ResponseData;
//import com.koi151.msproperty.service.PropertiesService;
//import com.koi151.msproperty.utils.SortUtil;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Min;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/properties")
//public class PropertyClientController {
//
//    private final PropertiesService propertiesService;
//    private final ResponseDataMapper responseDataMapper;
//
//    private static final int MAX_PAGE_SIZE = 20;
//    @GetMapping("/")
//    @PreAuthorize("hasAuthority('SCOPE_properties_view')")
//    public ResponseEntity<ResponseData> searchPropertiesForClient(
//        @RequestParam(required = false, defaultValue = "1")
//        @Min(value = 1, message = "Page number must be at least 1") int page,
//
//        @RequestParam(required = false, defaultValue = "10")
//        @Min(value = 1, message = "Page size must be at least 1") int limit,
//
//        @RequestParam(required = false) String[] sort,
//
//        @ModelAttribute @Valid PropertySearchRequest request
//    ) {
//        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
//
//        Sort sorting = SortUtil.createSortOrders(sort);
//        Pageable pageable = PageRequest.of(page - 1, pageSize, sorting);
//
//        var propertiesPage = propertiesService.searchPropertiesForClient(request, pageable);
//
//        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
//        responseData.setDesc(propertiesPage.isEmpty()
//            ? "No property found"
//            : "Get properties succeed");
//
//        return ResponseEntity.ok(responseData);
//    }
//}
