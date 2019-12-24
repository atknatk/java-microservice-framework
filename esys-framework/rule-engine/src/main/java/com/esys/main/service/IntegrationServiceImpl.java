package com.esys.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.main.exception.UnAuthorizedException;
import com.esys.main.security.TokenHelper;
import com.esys.main.utils.JwtTokenDataHolder;

@Service("integrationService")
public class IntegrationServiceImpl implements IntegrationService {


	@Autowired
	TokenHelper tokenHelper;
	
	@Override
	public String getToken(Map<String,Object> parameterMap){
		
		String platform = (String) JwtTokenDataHolder.getInstance().getPlatform();
		Integer tokenExpiry = (Integer) JwtTokenDataHolder.getInstance().getTokenExpiry();
		if (platform == null) {
			throw new UnAuthorizedException("You Don't Have Permission");
		}	
		
		if (parameterMap == null) {
			parameterMap = new HashMap<String, Object>();
		}	
		parameterMap.put("platform", platform);
		parameterMap.put("tokenExpiry", tokenExpiry);
		String jwt = tokenHelper.generateToken(parameterMap, tokenExpiry);
		
		return jwt;
	}
		
}