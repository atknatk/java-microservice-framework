package com.esys.main.enums;

import javax.persistence.ParameterMode;

public enum ParameterModeEnum {
    IN("IN",ParameterMode.IN),
    OUT("OUT",ParameterMode.OUT),
    INOUT("INOUT",ParameterMode.INOUT),
    REFCURSOR("REFCURSOR",ParameterMode.REF_CURSOR);
    
    private ParameterMode value = null;
    private String parameterMode = null;
	
	public ParameterMode value() {
		return this.value;
	}
	
	private ParameterModeEnum(String parameterMode,ParameterMode value) {
		this.parameterMode = parameterMode;
		this.value = value;
	}
	
	public static ParameterModeEnum fromParameterMode(String v) {
		for (ParameterModeEnum c : values()) {
			if (c.parameterMode.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
    
   
}
