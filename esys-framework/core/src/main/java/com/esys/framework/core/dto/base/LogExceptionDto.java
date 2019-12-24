package com.esys.framework.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
@ToString
public class LogExceptionDto extends AbstractDto {

    private String service;
    private String method;
    private Long executionTime;
    private String ip;
    private String client;
    private String browser;
    private String exception;
    private String module;

    private String userAgent;
    private String message;
    private String OS;
    private boolean success;

    @JsonFormat(pattern="dd.MM.yyyy hh:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime createdDate;

    private String createdBy;
}
