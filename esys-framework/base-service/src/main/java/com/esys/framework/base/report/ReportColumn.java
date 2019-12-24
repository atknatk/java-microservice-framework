package com.esys.framework.base.report;

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import lombok.Data;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
public class ReportColumn {

    private String messageKey;
    private String defaultName;
    private int width;
    private String className;
    private boolean fixedWidth = false;
    private String pattern;

    public ReportColumn(String messageKey, String defaultName, int width, String className) {
        this.messageKey = messageKey;
        this.defaultName = defaultName;
        this.width = width;
        this.className = className;
    }

    public ReportColumn(String messageKey, String defaultName, int width, String className, boolean fixedWidth, String pattern) {
        this.messageKey = messageKey;
        this.defaultName = defaultName;
        this.width = width;
        this.className = className;
        this.fixedWidth = fixedWidth;
        this.pattern = pattern;
    }

    public ReportColumn() {
    }
}
