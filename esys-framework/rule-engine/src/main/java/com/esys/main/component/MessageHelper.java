package com.esys.main.component;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

	@Autowired
	private MessageSource messageSource;


	public String getMessage(String messageCode,Object... messageArguments) {
		return messageSource.getMessage(messageCode, messageArguments, new Locale("tr"));
	}
	
	public String getMessage(String messageCode){
		return getMessage(messageCode,null);
	}
}
