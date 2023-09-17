package com.wonnapark.wnpserver.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6)")
    @CreatedDate
    LocalDateTime createAt;

    @Column(name = "update_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    @LastModifiedDate
    LocalDateTime updateAt;
}
