package com.esys.framework.core.entity.core;

import com.esys.framework.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class LogEvent extends BaseEntity {

    private String service;
    private String method;
    private Long executionTime;
    private String ip;
    private String client;
    private String browser;
    private String traceId;
    private String spanId;
    private String module;

    @Column(columnDefinition="TEXT")
    private String userAgent;

    @Column(columnDefinition="TEXT")
    private String parameter;

    @Column(columnDefinition="TEXT")
    private String returnValue;

    private String OS;
    private boolean success = false;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now();

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

}
