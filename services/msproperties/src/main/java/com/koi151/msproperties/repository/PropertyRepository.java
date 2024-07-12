package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer>, PropertyRepositoryCustom {

    Page<PropertyEntity> findByDeleted(boolean deleted, PageRequest request);
    Page<PropertyEntity> findByCategoryIdAndDeleted(Integer categoryId, boolean deleted, PageRequest request);
    Page<PropertyEntity> findByAccountIdAndDeleted(Long accountId, boolean deleted, Pageable pageable);
    Page<PropertyEntity> findByStatus(StatusEnum status, Pageable pageRequest);
    Optional<PropertyEntity> findByPropertyIdAndDeleted(Long propertyId, boolean deleted);
}
