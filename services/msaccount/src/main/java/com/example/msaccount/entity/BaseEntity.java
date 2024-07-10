package com.example.msaccount.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
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
    private static final long serialVersionUID = 4746498586807182115L;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP(0)")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_date", columnDefinition = "TIMESTAMP(0)")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "deleted")
    private boolean deleted = false;
}