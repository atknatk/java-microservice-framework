 package com.esys.main.controller.input;

import java.util.List;
import java.util.Map;

import lombok.Data;


@Data
public class CallServiceInput {
	 		
	
		private String body;
		private String httpMethod;
		private String url;
		private String contentType;
		private Map<String,List<String>> headers;
}
