package com.esys.framework.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
@ToString
public class LogEventDto extends AbstractDto {

    private String service;
    private String method;
    private Long executionTime;
    private String ip;
    private String client;
    private String browser;
    private String traceId;
    private String spanId;
    private String module;

    private String userAgent;
    private String parameter;
    private String returnValue;
    private String OS;
    private boolean success;

    @JsonFormat(pattern="dd.MM.yyyy hh:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime createdDate;

    private String createdBy;
}
