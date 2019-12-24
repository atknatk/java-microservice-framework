package com.esys.framework.core.entity.core;

import com.esys.framework.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class LogException extends BaseEntity {

    private String service;
    private String method;
    private String ip;
    private String client;
    private String browser;
    private String module;

    @Column(columnDefinition="TEXT")
    private String exception;

    @Column(columnDefinition="TEXT")
    private String message;


    @Column(columnDefinition="TEXT")
    private String userAgent;


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
