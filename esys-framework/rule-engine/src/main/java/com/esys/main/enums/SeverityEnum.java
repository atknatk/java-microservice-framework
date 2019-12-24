package com.esys.main.enums;

public enum SeverityEnum {
    
    ERROR("ERROR"),
    SUCCSESS("SUCCSES"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    INFO("INFO"),
    WARNING("WARNING");
    
    private String value = null;
	
	public String value() {
		return this.value;
	}
	
	private SeverityEnum(String val) {
		this.value = val;
	}
	
	public static SeverityEnum fromValue(String v) {
		for (SeverityEnum c : values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
    
   
}
