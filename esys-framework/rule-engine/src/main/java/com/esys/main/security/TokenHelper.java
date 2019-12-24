package com.esys.main.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import com.auth0.jwt.JWTSigner;
//import com.auth0.jwt.JWTSigner.Options;
import com.esys.main.utils.JwtTokenDataHolder;
//import com.auth0.jwt.JWTVerifier;



@Component
public class TokenHelper {

	@Value("${jwt.university-portal.secret.integration.sha}")
	public String SECRET_INTEGRATION;
	
	@Value("${jwt.university-portal.secret.external-server.sha}")
	public String SECRET_EXTERNAL_INTEGRATION;
	
	@Value("${jwt.university-portal.secret.external-client.sha}")
	public String SECRET_EXTERNAL_CLIENT_INTEGRATION;
	
	@Value("${jwt.university-portal.secret.external-js-client.sha}")
	public String SECRET_EXTERNAL_JS_CLIENT_TOKEN;
	
	@Value("${jwt.university-portal.secret.token.sha}")
	public String SECRET_TOKEN;

	@Value("${jwt.header}")
	private String AUTH;
	
	@Value("${jwt.university-portal.integrationKey}")
	private String INTEGRATION_KEY_UNIVERSITY_PORTAL;

	public String getUsernameFromToken(String token) throws Exception {
		String username = null;
		Map<String,Object> claims = this.getAllClaimsFromToken(token);
		username = (String) claims.get("username");
		return username;
	}

	public String getPlatformFromToken(String token) throws Exception {
		String platform = null;
		Map<String,Object> claims = this.getAllClaimsFromToken(token);
		platform = (String) claims.get("platform");
		return platform;

	}
	
	public String getPlatformFromIntegrationKey(String integrationKey) throws Exception {
		String platform = null;
		Map<String,Object> claims = this.getAllClaimsFromIntegration(integrationKey);
		platform = (String) claims.get("platform");
		return platform;

	}
	
	public String getPlatformFromExternalKey(String externalKey) throws Exception {
		String platform = null;
		Map<String,Object> claims = this.getAllClaimsFromExternalIntegration(externalKey);
		platform = (String) claims.get("platform");
		return platform;

	}
	
	public String getPlatformFromExternalClientKey(String externalClientKey) throws Exception {
		String platform = null;
		Map<String,Object> claims = this.getAllClaimsFromExternalIntegration(externalClientKey);
		platform = (String) claims.get("platform");
		return platform;

	}
	
	
	public Boolean getAuthFromToken(String token) throws Exception {
		Boolean auth = null;
		Map<String,Object> claims = this.getAllClaimsFromToken(token);
		auth = (Boolean) claims.get("auth");
		return auth;
	}

	

	public String generateToken(Map<String, Object> claims,Integer expirySeconds) {
//		Options option = new Options();
//		option.setExpirySeconds(expirySeconds);
//		
//		JWTSigner signer = new JWTSigner(SECRET_TOKEN);
//		String newToken = signer.sign(claims, option);
//	    return newToken;
		return null;
	}
	
	public String generateExternalClientToken(Map<String, Object> claims,Integer expirySeconds) {
//		Options option = new Options();
//		option.setExpirySeconds(expirySeconds);
//		
//		JWTSigner signer = new JWTSigner(SECRET_EXTERNAL_JS_CLIENT_TOKEN);
//		String newToken = signer.sign(claims, option);
//	    return newToken;
		return null;
	}
	
	public String generateIntegrationKey(Map<String, Object> claims) {
//		JWTSigner signer = new JWTSigner(SECRET_INTEGRATION);
//		String newIntegrationKey = signer.sign(claims,  new Options());
//		return newIntegrationKey;
		return null;
	}
	
	public String generateExternalIntegrationKey(Map<String, Object> claims) {
//		JWTSigner signer = new JWTSigner(SECRET_EXTERNAL_INTEGRATION);
//		String newIntegrationKey = signer.sign(claims,  new Options());
//		return newIntegrationKey;
		return null;
	}
	
	public String generateExternalClientIntegrationKey(Map<String, Object> claims) {
//		JWTSigner signer = new JWTSigner(SECRET_EXTERNAL_CLIENT_INTEGRATION);
//		String newIntegrationKey = signer.sign(claims,  new Options());
//		return newIntegrationKey;
		return null;
	}

	public Map<String,Object> getAllClaimsFromToken(String token) throws Exception {
//		 Map<String,Object> data = new JWTVerifier(SECRET_TOKEN).verify(token);
//		return data;
		return null;
	}
	
	public Map<String,Object> getAllClaimsFromIntegration(String integrationKey) throws Exception {
//		Map<String,Object> data = new JWTVerifier(SECRET_INTEGRATION).verify(integrationKey);
//		return data;
		return null;
	}
	
	public Map<String,Object> getAllClaimsFromExternalIntegration(String integrationKey) throws Exception {
//		Map<String,Object> data = new JWTVerifier(SECRET_EXTERNAL_INTEGRATION).verify(integrationKey);
//		return data;
		return null;
	}
	
	public Map<String,Object> getAllClaimsFromExternalClientIntegration(String integrationKey) throws Exception {
//		Map<String,Object> data = new JWTVerifier(SECRET_EXTERNAL_CLIENT_INTEGRATION).verify(integrationKey);
//		return data;
		return null;
	}
	
	public Map<String,Object> getAllClaimsFromExternalJSClientToken(String token) throws Exception {
//		Map<String,Object> data = new JWTVerifier(SECRET_EXTERNAL_JS_CLIENT_TOKEN).verify(token);
//		return data;
		return null;
	}
	
	public Map<String,Object> getAllClaimsFromExternalClientToken(String externalClientToken,String externalClientKey) throws Exception {
//		Map<String,Object> data = new JWTVerifier(externalClientKey).verify(externalClientToken);
//		return data;
		return null;
	}
	
	public boolean validateIntegrastionKey(String integrationKey) {
		if(integrationKey != null && integrationKey.equals(INTEGRATION_KEY_UNIVERSITY_PORTAL))
			return true;
		
		return false;
	}
	
	public  String refreshToken() throws Exception{
		try {	
			
			Map<String,Object> claims = JwtTokenDataHolder.getInstance().getData();
			Integer tokenExpiry = JwtTokenDataHolder.getInstance().getTokenExpiry();
			return generateToken(claims, tokenExpiry);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	public String getToken(HttpServletRequest request) {
		/**
		 * Getting the token from Authentication header e.g Bearer your_token
		 */
		String authHeader = getAuthHeaderFromHeader(request);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	private String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH);
	}
}