package com.esys.main.enums;

public enum AppointmentStatuEnum {
	ONLINE("ONLINE"),
	OFFLINE("OFFLINE"),
	ALL("ALL");
	
	private String value = null;
	
	public String value() {
		return this.value;
	}
	
	private AppointmentStatuEnum(String val) {
		this.value = val;
	}
	
	public static AppointmentStatuEnum fromValue(String v) {
		for (AppointmentStatuEnum c : values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
