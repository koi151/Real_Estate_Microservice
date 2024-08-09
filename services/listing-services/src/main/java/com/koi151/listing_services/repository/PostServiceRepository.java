package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostServiceRepository extends JpaRepository<PostService, Long> {
    boolean existsByName(String name);
}
