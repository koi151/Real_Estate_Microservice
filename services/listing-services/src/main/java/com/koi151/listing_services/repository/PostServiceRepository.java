package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostServiceRepository extends JpaRepository<PostService, Long> {
    boolean existsByName(String name);
    boolean existsAllByPostServiceIdIn(List<Long> ids);

    @Query("SELECT ps.postServiceId FROM post_service ps WHERE ps.postServiceId NOT IN :ids") // jpql
    List<Long> findIdsByPostServiceIdNotIn(@Param("ids") List<Long> ids);
}
