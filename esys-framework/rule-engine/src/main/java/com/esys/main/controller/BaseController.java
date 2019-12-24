package com.esys.main.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;

import com.esys.main.conf.CustomClientHttpRequestInterceptor;
import com.esys.main.controller.output.BaseOutput;
import com.esys.main.controller.output.ResultMessages;
import com.esys.main.enums.SeverityEnum;

@Controller
@Transactional
public class BaseController {
		
	@Autowired
	private MessageSource messageSource;

	public  List<ResultMessages> getValidationMessages(BindingResult result) {
		List<ObjectError> fieldErrorList= result.getGlobalErrors();
		List<ResultMessages> errorMessages= new ArrayList<ResultMessages>();
		for(ObjectError fieldError: fieldErrorList ){   
		
			String message;
			Locale locale=new Locale("TR");
			String objectName="";
			String severity=SeverityEnum.ERROR.value();
			if(fieldError.getArguments()!=null && fieldError.getArguments().length > 0) {
				
				if(fieldError.getArguments()[0] instanceof SeverityEnum) {
					SeverityEnum severityEnum=(SeverityEnum)fieldError.getArguments()[0];
					severity=severityEnum.value();
				} else if (SeverityEnum.fromValue((fieldError.getArguments()[0]).toString()) != null) {
					severity = (fieldError.getArguments()[0]).toString();
				}
				
				if(fieldError.getArguments().length > 1) {
					objectName=fieldError.getArguments()[1].toString();
				}	
			}
			try {
				message=messageSource.getMessage(fieldError.getCodes()[1].toString(), new Object[]{objectName}, locale);
			} catch (Exception e) {
				message=fieldError.getDefaultMessage();
			}

			ResultMessages	resultMessages = new ResultMessages(
					new Date(), severity,
					message,
					"validationError",
					"",
					"",//fieldError.getField(),
					"");
			errorMessages.add(resultMessages);
		}
		return errorMessages;
	}
	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new CustomClientHttpRequestInterceptor()));
		return restTemplate;
	}
	
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json; charset=UTF-8");
		headers.set("accept", "application/json, text/plain, */*");
		headers.set("Auth", getToken());
		return headers;
	}

	public String getToken() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "Bearer "+authentication.getCredentials();
	}

	public String nullControl(String obj) {
		if (obj == null) {
			return "";
		}
		return obj;
	}

	public <T> ResponseEntity responseEntity(T entity) {
		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}
	
	public <T> ResponseEntity responseEntityWithHeader(T entity,String header) {
		return ResponseEntity.status(HttpStatus.OK).header("Auth", header).body(entity);
	}
	
	public <T> ResponseEntity responseOK() {
		return ResponseEntity.status(HttpStatus.OK).body(getMessageBody(HttpStatus.OK));
	}
	
	public <T> ResponseEntity responseWithStatus(HttpStatus status) {
		
		return ResponseEntity.status(status).body(getMessageBody(status));
	}
	private BaseOutput getMessageBody(HttpStatus status) {
		BaseOutput response = new BaseOutput();
		response.setReturnCode(""+status.value());
		response.setReturnMessage(status.getReasonPhrase());
		return response;
	}

}
