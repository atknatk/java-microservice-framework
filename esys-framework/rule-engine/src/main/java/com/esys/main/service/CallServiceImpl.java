package com.esys.main.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.esys.main.conf.CustomClientHttpRequestInterceptor;
import com.esys.main.controller.input.CallServiceInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.action.CallServiceOutput;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("restCallService")
public class CallServiceImpl implements CallService {
	
	

	@Override
	public OutputDTO<CallServiceOutput> callRest(CallServiceInput input) {
		//Spring validasyon eklenecek
 		OutputDTO<CallServiceOutput> output = new OutputDTO<CallServiceOutput>();
		
		String url = input.getUrl();
		String httpMethod = input.getHttpMethod();
		
		String json = input.getBody();	
			
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new CustomClientHttpRequestInterceptor()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(!CollectionUtils.isEmpty(input.getHeaders())) {
			for(Map.Entry<String, List<String>> header : input.getHeaders().entrySet()) {
				headers.put(header.getKey(), header.getValue());
			}
		}
		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		ResponseEntity<Map<String, Object>> response =  restTemplate.exchange(url, HttpMethod.resolve(httpMethod), entity, new ParameterizedTypeReference<Map<String, Object>>() {});
		
		CallServiceOutput callServiceOutput = new CallServiceOutput();
		callServiceOutput.setStatus(response.getStatusCode().getReasonPhrase());
		callServiceOutput.setStatusCode(response.getStatusCode().value());
		String responseBodyJson = "";
		Map<String, Object> reponseBody = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		if(!CollectionUtils.isEmpty(reponseBody)) {
			try {
				responseBodyJson = mapper.writeValueAsString(reponseBody);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}	
		HttpHeaders outputHeaders = response.getHeaders();
		if(outputHeaders != null && !CollectionUtils.isEmpty(outputHeaders.entrySet())) {
			Map<String,List<String>> headerMap = new HashMap<String,List<String>>();
			for(Map.Entry<String, List<String>> entry : outputHeaders.entrySet()) {
				headerMap.put(entry.getKey(), entry.getValue());
			}
			callServiceOutput.setHeaders(headerMap);
		}
		callServiceOutput.setBody(responseBodyJson);
		callServiceOutput.setTime(new Date());
		
		output.addSuccessMessage("Call Request Succsess");
		output.setOutputData(callServiceOutput);
		return output;
	}

}
