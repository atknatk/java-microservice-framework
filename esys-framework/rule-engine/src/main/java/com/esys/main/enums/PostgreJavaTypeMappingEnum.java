package com.esys.main.enums;

public enum PostgreJavaTypeMappingEnum {
    
	BIGINT("bigint",Long.class),
	BIT("bit",java.lang.Boolean.class),
	BITVARYING("bit",String.class),
	BOOLEAN("boolean",java.lang.Boolean.class),
	BYTEA("bytea",Byte[].class),
	CHARACTER("character",String.class),
	CHARACTERVARYING("character varying",String.class),
	DATE("date",java.util.Date.class),
	DOUBLEPRECISION("double precision",Double.class),
	INTEGER("integer",Integer.class),
	NUMERIC("numeric",java.math.BigDecimal.class),
	SMALLINT("smallint",Integer.class),
	TEXT("text",String.class),
	TIME("time",java.util.Date.class),
	TIMESTAMP("timestamp",java.util.Date.class);
    
    private String postgreType = null;
    private Class<?> javaType = null;
    
	public String postgreType() {
		return this.postgreType;
	}
	
	public Class<?> javaType() {
		return this.javaType;
	}
	
	private PostgreJavaTypeMappingEnum(String postgreType,Class<?> javaType) {
		this.postgreType = postgreType;
		this.javaType= javaType;
	}
	
	public static PostgreJavaTypeMappingEnum fromPostgreType(String postgreType) {
		for (PostgreJavaTypeMappingEnum c : values()) {
			if (c.postgreType.equals(postgreType)) {
				return c;
			}
		}
		throw new IllegalArgumentException(postgreType);
	}
    
   
}
