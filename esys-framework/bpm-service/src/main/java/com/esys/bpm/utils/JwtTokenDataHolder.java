package com.esys.bpm.utils;

import java.util.Map;

public class JwtTokenDataHolder {

	private static JwtTokenDataHolder dataHolder;

	private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	private JwtTokenDataHolder() {
	}

	public void setData(Map<String, Object> data) {
		threadLocal.set(data);
	}

	public Map<String, Object> getData() {
		return threadLocal.get();
	}

	public String getValue(String key){
		if(threadLocal.get() != null){
			return (String) threadLocal.get().get(key);
		}
		return null;
	}

	public String getPlatform(){
		return getValue("platform");
	}
	
	public String getToken(){
		return getValue("token");
	}
	
	public String getUsername(){
		return getValue("username");
	}
	
	public String getTraceId(){
		return getValue("traceId");
	}
	
	public Integer getTokenExpiry(){
		if(threadLocal.get() != null){
			return (Integer) threadLocal.get().get("tokenExpiry");
		}
		return null;
	}
	
	public static JwtTokenDataHolder getInstance() {
		if (dataHolder == null) {
			dataHolder = new JwtTokenDataHolder();
		}
		return dataHolder;
	}

}
