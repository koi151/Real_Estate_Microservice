package com.koi151.msproperties.repository;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Integer> {
    Page<Properties> findByDeleted(boolean deleted, PageRequest request);
    Page<Properties> findByCategoryIdAndDeleted(Integer categoryId, boolean deleted, PageRequest request);
    Page<Properties> findByStatusEnum(StatusEnum status, PageRequest pageRequest);
    Optional<Properties> findByIdAndDeleted(Integer categoryId, boolean deleted);
}
