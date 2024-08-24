package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.repository.custom.PostServiceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PostServiceRepository extends JpaRepository<PostService, Long>, PostServiceRepositoryCustom {
    boolean existsByName(String name);

    @Query("SELECT new com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO(ps.postServiceId, ps.name, ps.availableUnits) " +
            "FROM post_service ps WHERE ps.postServiceId IN :ids")
    List<PostServiceBasicInfoDTO> getNameAndAvailableUnitsById(@Param("ids") Iterable<Long> ids);
}
