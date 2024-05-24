package com.koi151.mspropertycategory.repository;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Integer> {
}
