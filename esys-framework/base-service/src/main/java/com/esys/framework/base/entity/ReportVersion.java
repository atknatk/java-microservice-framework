package com.esys.framework.base.entity;

import com.esys.framework.base.enums.ReportName;
import com.esys.framework.core.entity.AbstractAuditingEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Entity
@Data
public class ReportVersion extends AbstractAuditingEntity {


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String data;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private byte[] sign;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private byte[] report;

    private int version;

    private boolean current;

    @Enumerated(EnumType.STRING)
    private ReportName reportName;

}
