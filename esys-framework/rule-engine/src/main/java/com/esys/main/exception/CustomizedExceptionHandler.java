package com.esys.main.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.esys.main.utils.JwtTokenDataHolder;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(BusinessException.class)
	public final ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex, WebRequest request) {
		String message = ex.getMessage(messageSource, request.getLocale());
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("message", message);
		
		String traceId = JwtTokenDataHolder.getInstance().getTraceId();
		log.warn("BusinessException TraceId:" + traceId + " Status Code:" + HttpStatus.UNPROCESSABLE_ENTITY.value()
				+ " " + HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase() + " Error Message:" + message);
		return new ResponseEntity<>(errorAttributes, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public final ResponseEntity<Map<String, Object>> handleUnAuthorizedExceptions(UnAuthorizedException ex, WebRequest request) {
		String message = ex.getMessage(messageSource, request.getLocale());
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("message", message);
		
		String traceId = JwtTokenDataHolder.getInstance().getTraceId();
		log.warn("UnAuthorizedException TraceId:" + traceId + " Status Code:" + HttpStatus.UNAUTHORIZED.value()
				+ " " + HttpStatus.UNAUTHORIZED.getReasonPhrase() + " Error Message:" + message);
		
		return new ResponseEntity<>(errorAttributes, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		String message = messageSource.getMessage(ex.getMessage(), null, request.getLocale());
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("message", message);
		
		String traceId = JwtTokenDataHolder.getInstance().getTraceId();
		log.warn("UsernameNotFoundException TraceId:" + traceId + " Status Code:" + HttpStatus.UNPROCESSABLE_ENTITY.value()
		+ " " + HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase() + " Error Message:" + message);
		return new ResponseEntity<>(errorAttributes, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
		String traceId = JwtTokenDataHolder.getInstance().getTraceId();
		log.error("TraceId:"+traceId + " Status Code:" + HttpStatus.INTERNAL_SERVER_ERROR.value() 
		+ " " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + " Error Message:",ex);
		
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("timestamp", new Date());
		errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorAttributes.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		errorAttributes.put("series", HttpStatus.INTERNAL_SERVER_ERROR.series().name());
		errorAttributes.put("message", ex.getMessage());

		return new ResponseEntity<>(errorAttributes, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
