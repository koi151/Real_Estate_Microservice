package com.koi151.mspropertycategory.repository;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Integer> {
    List<PropertyCategory> findByTitleContainingIgnoreCase(String title);
    Page<PropertyCategory> findByStatusEnum(StatusEnum statusEnum, PageRequest pageRequest);
}
