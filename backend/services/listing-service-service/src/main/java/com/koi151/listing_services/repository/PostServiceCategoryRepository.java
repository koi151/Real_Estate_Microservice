package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostServiceCategoryRepository extends JpaRepository<PostServiceCategory, Integer> {
    boolean existsByName(String name);

}
