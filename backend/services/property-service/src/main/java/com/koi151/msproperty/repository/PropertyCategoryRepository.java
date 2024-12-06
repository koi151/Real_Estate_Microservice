package com.koi151.msproperty.repository;

import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.repository.custom.PropertyCategoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Long>, PropertyCategoryRepositoryCustom {
    Optional<PropertyCategory> findByCategoryIdAndDeleted(Long categoryId, boolean deleted);
    Optional<PropertyCategory> findByCategoryIdAndDeleted(Integer id, boolean deleted);
    Page<PropertyCategory> findByStatus(StatusEnum statusEnum, Pageable pageable);
    @Query("SELECT c FROM property_category c WHERE c.parentCategory IS NULL AND c.deleted = false")
    List<PropertyCategory> findAllRootCategories();
    List<PropertyCategory> findPropertyCategoryByDeletedFalse();
}
