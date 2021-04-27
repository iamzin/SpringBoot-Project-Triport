package com.project.triport.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // get 함수 자동 생성
@MappedSuperclass // 멘버 변수가 컬럼이 되도록 함
@EntityListeners(AuditingEntityListener.class) // 변경 시 자동 기록
public abstract class Timestamped {
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
