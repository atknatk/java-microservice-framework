package com.esys.main.controller.output.action;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageOutput extends BaseActionOutput{

	private Date timestamp;
	private String severity;
	private String messageText;
	private String type;	
}
