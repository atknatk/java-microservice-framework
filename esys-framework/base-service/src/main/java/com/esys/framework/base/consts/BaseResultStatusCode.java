package com.esys.framework.base.consts;

import com.esys.framework.core.consts.ResultStatusCode;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface BaseResultStatusCode extends ResultStatusCode {

    int _totalizer = 1000;
    int ReportGenerationError = 1+ _totalizer;
}
