package com.koi151.msproperties.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6097183753510743317L;

//    @Convert(converter = LocalDateTimeConverter.class)
    @CreatedDate
    @Column(name = "created_date", columnDefinition = "TIMESTAMP(0)",
        updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", columnDefinition = "TIMESTAMP(0)", insertable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "deleted")
    private boolean deleted = false;
}













