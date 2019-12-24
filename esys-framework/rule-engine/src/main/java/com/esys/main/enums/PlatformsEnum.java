package com.esys.main.enums;

public enum PlatformsEnum {
    
    ALZ_ANDROID("ALZ_ANDROID",2700000),//1 ay accenture'ın dediğine göre timeoutları 1 aymış.,
    ALZ_IOS("ALZ_IOS",2700000),//1 ay accenture'ın dediğine göre timeoutları 1 aymış.,
    ALZ_OTOANALIZ("ALZ_OTOANALIZ",3000),
    ALZ_HASAR("ALZ_HASAR",3000),
    ALZ_AZNET("ALZ_AZNET",3000),
    ASP("ASP",3000),
    DAHI_AI("DAHI_AI",3000),
    INFINITE("INFINITE",0), 
    CM_CLIENT("CM_CLIENT",0),
    CM_AGENT("CM_AGENT",0),
    WEBCHAT_SB("WEBCHAT_SB",0);
   
    private String platformCode;
 
    private int platformTimeout;//second
    
    private PlatformsEnum(String platformCode,int platformTimeout) {
            this.platformCode = platformCode;
            this.platformTimeout = platformTimeout;
    }

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public int getPlatformTimeout() {
		return platformTimeout;
	}

	public void setPlatformTimeout(int platformTimeout) {
		this.platformTimeout = platformTimeout;
	}
    
   
}
