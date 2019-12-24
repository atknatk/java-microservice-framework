package com.esys.main.controller.output;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessages {

	private Date timestamp;
	private String severity;
	private String messageText;
	private String type;
	private String details;
	private String field;
	private String rejectedValue;	
}
