package com.esys.framework.base.enums;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public enum  ImportSectionEnum {
    PRODUCT("Product");

    final String typeValue;

    private ImportSectionEnum(final String typeValue) {
        this.typeValue = typeValue;
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return typeValue;
    }

    @Override
    public String toString() {
        return name();
    }

}
