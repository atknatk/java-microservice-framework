package com.esys.framework.base.dto;

import com.esys.framework.base.enums.ReportName;
import com.esys.framework.core.dto.base.AbstractDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
public class ReportVersionDto extends AbstractDto {


    private int version;

    private ReportName reportName;

    private String createdBy;

    private LocalDateTime createdDate;

    private boolean current;

    public ReportVersionDto() {
    }

    public ReportVersionDto(int version, ReportName reportName, String createdBy, LocalDateTime createdDate) {
        this.version = version;
        this.reportName = reportName;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public ReportVersionDto(Long id, int version, ReportName reportName, String createdBy, LocalDateTime createdDate, boolean current) {
        super(id);
        this.version = version;
        this.reportName = reportName;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.current = current;
    }
}
