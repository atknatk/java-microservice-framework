package com.esys.framework.base.enums;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public enum ReportName {


    PRODUCT("productReport");


    private final String text;
    ReportName(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
