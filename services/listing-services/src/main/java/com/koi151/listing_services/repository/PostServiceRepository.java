package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.repository.custom.PostServiceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostServiceRepository extends JpaRepository<PostService, Long>, PostServiceRepositoryCustom {
    boolean existsByName(String name);
}
