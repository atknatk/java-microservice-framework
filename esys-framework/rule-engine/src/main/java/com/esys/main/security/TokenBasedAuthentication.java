package com.esys.main.security;

import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenBasedAuthentication extends AbstractAuthenticationToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
    private final UserDetails principle;
    private String userName;
    private Map<String,Object> tokenClaims;

    public TokenBasedAuthentication( UserDetails principle ) {
        super( principle.getAuthorities() );
        this.principle = principle;
    }
    
    public TokenBasedAuthentication( UserDetails principle ,Map<String,Object> tokenClaims) {
        super( principle.getAuthorities() );
        this.principle = principle;
        this.tokenClaims = tokenClaims;
    }

    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principle;
    }

	public Map<String,Object> getTokenClaims() {
		return tokenClaims;
	}

	public void setTokenClaims(Map<String,Object> tokenClaims) {
		this.tokenClaims = tokenClaims;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
