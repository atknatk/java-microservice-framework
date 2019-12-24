package com.esys.main.enums;

public enum PostgreSqlTypeMappingEnum {
    
	BIGINT("bigint",java.sql.Types.BIGINT),
	BIT("bit",java.sql.Types.BIT),
	BOOLEAN("boolean",java.sql.Types.BOOLEAN),
	CHARACTER("character",java.sql.Types.VARCHAR),
	CHARACTERVARYING("character varying",java.sql.Types.VARCHAR),
	DATE("date",java.sql.Types.DATE),
	DOUBLEPRECISION("double precision",java.sql.Types.DOUBLE),
	INTEGER("integer",java.sql.Types.INTEGER),
	NUMERIC("numeric",java.sql.Types.NUMERIC),
	SMALLINT("smallint",java.sql.Types.SMALLINT),
	TEXT("text",java.sql.Types.VARCHAR),
	REF_CUR("refcursor",java.sql.Types.REF_CURSOR),
	TIME("time",java.sql.Types.TIME),
	TIMESTAMP("timestamp",java.sql.Types.TIMESTAMP);
    
    private String postgreType = null;
    private int sqlType;
    
	public String postgreType() {
		return this.postgreType;
	}
	
	public int sqlType() {
		return this.sqlType;
	}
	
	private PostgreSqlTypeMappingEnum(String postgreType,int sqlType) {
		this.postgreType = postgreType;
		this.sqlType= sqlType;
	}
	
	public static PostgreSqlTypeMappingEnum fromPostgreType(String postgreType) {
		for (PostgreSqlTypeMappingEnum c : values()) {
			if (c.postgreType.equals(postgreType)) {
				return c;
			}
		}
		throw new IllegalArgumentException(postgreType);
	}
    
   
}
