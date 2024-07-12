package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.repository.custom.PropertyCategoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategoryEntity, Integer>, PropertyCategoryRepositoryCustom {
    List<PropertyCategoryEntity> findByTitleContainingIgnoreCase(String title);
    Optional<PropertyCategoryEntity> findByCategoryIdAndDeleted(Integer id, boolean deleted);
    Page<PropertyCategoryEntity> findByStatus(StatusEnum statusEnum, Pageable pageable);
}
