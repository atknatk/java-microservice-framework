package com.esys.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.exception.UnAuthorizedException;
import com.esys.main.security.TokenHelper;
import com.esys.main.service.IntegrationService;
import com.esys.main.utils.JwtTokenDataHolder;


@RestController
@RequestMapping(value = "/integration/integrationController", consumes = "application/json")
public class IntegrationController extends BaseController {

	@Autowired
	TokenHelper tokenHelper;
	
	@Autowired
	IntegrationService integrationService;

	public IntegrationController() {
		// TODO Auto-generated constructor stub
	}
	
	
	@PostMapping(path = "/getToken")
	public @ResponseBody ResponseEntity<String> getToken(@RequestBody Map<String, Object> parameterMap) {
		
		if (JwtTokenDataHolder.getInstance().getData() != null) {
			String jwt = integrationService.getToken(parameterMap);
			return responseEntity(jwt);
		} else {
			throw new UnAuthorizedException("You Don't Have Permission");
		}

	}
	
	
	@Profile("DEV")
	@PostMapping(path = "/generateIntegration")
	public @ResponseBody ResponseEntity<?> generateIntegration(@RequestBody Map<String, Object> parameterMap) {
		String platform = (String) parameterMap.get("platform");
		
		if(platform != null){
//			Integer integrationExpiry = (Integer) parameterMap.get("integrationExpiry");
			Integer tokenExpriy = (Integer) parameterMap.get("tokenExpiry");
			//String startDate = (String) parameterMap.get("startDate");
			//String endDate = (String) parameterMap.get("endDate");
			String explanation = (String) parameterMap.get("explanation");
			
			Map<String, Object> claims = new HashMap<String,Object>();
			claims.put("platform", platform);
			//claims.put("startDate",startDate);
			//claims.put("endDate", endDate);
			//claims.put("integrationExpiry", integrationExpiry);
			claims.put("tokenExpiry",tokenExpriy);
			claims.put("explanation",explanation);
			String integrationKey = tokenHelper.generateIntegrationKey(claims);
			
			return ResponseEntity.ok().body("integration key:"+integrationKey);
		}
		return null;
	}
	
	@Profile("DEV")
	@PostMapping(path = "/generateToken")
	public @ResponseBody ResponseEntity<?> generateToken(@RequestBody Map<String, Object> parameterMap) {
		String platform = (String) parameterMap.get("platform");
		Integer tokenExpiry = (Integer) parameterMap.get("tokenExpiry");
		if(platform != null){		
			String jwt = tokenHelper.generateToken(parameterMap,tokenExpiry);
			
			return ResponseEntity.ok().body("Token:"+jwt);
		}
		return null;
	}


}