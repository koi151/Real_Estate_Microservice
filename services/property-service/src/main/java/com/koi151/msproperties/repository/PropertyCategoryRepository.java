package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.PropertyCategory;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.repository.custom.PropertyCategoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Integer>, PropertyCategoryRepositoryCustom {
    Optional<PropertyCategory> findByCategoryIdAndDeleted(Integer id, boolean deleted);
    Page<PropertyCategory> findByStatus(StatusEnum statusEnum, Pageable pageable);
}
