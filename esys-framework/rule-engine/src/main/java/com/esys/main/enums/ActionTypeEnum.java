package com.esys.main.enums;

public enum ActionTypeEnum {
    
    CALL_SERVICE("CALL_SERVICE"),
    KEY_VALUE("KEY_VALUE"),
    MESSAGE("MESSAGE"),
    SQL("SQL");
    
    private String value = null;
	
	public String value() {
		return this.value;
	}
	
	private ActionTypeEnum(String val) {
		this.value = val;
	}
	
	public static ActionTypeEnum fromValue(String v) {
		for (ActionTypeEnum c : values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
    
   
}
