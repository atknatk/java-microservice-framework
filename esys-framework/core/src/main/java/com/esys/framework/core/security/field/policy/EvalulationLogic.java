package com.esys.framework.core.security.field.policy;

public enum  EvalulationLogic {
    /**
     * Tamamı eşleşirse izin verilir.
     */
    AND,

    /**
     * En az bir tanesi eşleşirse izin verilir
     */
    OR,

    /**
     * Sadece birisi eşlenirse izin verilir
     */
    XOR
}
