package com.esys.framework.base.reader.excel;

import lombok.Data;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
public class ExcelValueConfig {
    private String excelHeader;
    private int excelIndex;
    private String excelColType;
    private String excelValue;
    private String pojoAttribute;

}
