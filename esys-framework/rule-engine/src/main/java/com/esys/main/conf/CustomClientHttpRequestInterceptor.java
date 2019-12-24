package com.esys.main.conf;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.esys.main.utils.JwtTokenDataHolder;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	 
	final static Logger log = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		
		String traceId = JwtTokenDataHolder.getInstance().getTraceId();
		
		//eger trace id bos ise uygulama init olurken cikilan servisler
		if(traceId == null) {
			traceId = "centosTrace";
		}
		
		HttpHeaders headers = request.getHeaders();
		headers.add("X-B3-TraceId", traceId);
        traceRequest(request, body);
        
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		log.debug("===========================request begin================================================");
		log.debug("Request to  : {}", request.getURI());
		log.debug("Method      : {}", request.getMethod());
		log.debug("Auth        : {}", request.getHeaders().get("Auth"));
		log.debug("Request body: {}", new String(body, "UTF-8"));
		log.debug("==========================request end================================================");
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		log.debug("============================response begin==========================================");
		log.debug("Response code  : {}", response.getStatusCode());
		log.debug("Status   text  : {}", response.getStatusText());
		log.debug("Auth           : {}", response.getHeaders().get("Auth"));
		log.debug("Response body  : {}",  response.getBody());
		log.debug("=======================response end=================================================");
	}

}