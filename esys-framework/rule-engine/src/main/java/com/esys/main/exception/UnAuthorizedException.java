package com.esys.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException  extends RuleEngineException{
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnAuthorizedException(String messageCode) {
    	super(messageCode);   
    }
}