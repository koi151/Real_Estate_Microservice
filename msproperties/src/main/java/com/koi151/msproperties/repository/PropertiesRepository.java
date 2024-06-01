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

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Integer> {
    Page<Properties> findByDeleted(boolean deleted, PageRequest pageRequest);
    Page<Properties> findByStatusEnum(StatusEnum status, PageRequest pageRequest);
    Page<Properties> findPropertiesByCategoryId(Integer categoryId, PageRequest pageRequest);
}
