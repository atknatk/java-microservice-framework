package com.esys.framework.base.enums;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public enum DataTypeEnum {

    DOUBLE("Double"), //
    INTEGER("Integer"), //
    STRING("String"), //
    FLOAT("Float"),
    DATE("Date");

    final String typeValue;

    private DataTypeEnum(final String typeValue) {
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
