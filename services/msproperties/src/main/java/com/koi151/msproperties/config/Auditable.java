//package com.koi151.msproperties.config;
//
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.MappedSuperclass;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//abstract class Auditable {
//
//    @CreatedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    protected LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    protected  LocalDateTime updatedBy;
//}
