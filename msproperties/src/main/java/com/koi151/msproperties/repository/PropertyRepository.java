package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {
    Page<PropertyEntity> findByDeleted(boolean deleted, PageRequest request);
    Page<PropertyEntity> findByCategoryIdAndDeleted(Integer categoryId, boolean deleted, PageRequest request);
    Page<PropertyEntity> findByStatus(StatusEnum status, PageRequest pageRequest);
    Optional<PropertyEntity> findByIdAndDeleted(Integer categoryId, boolean deleted);
}
