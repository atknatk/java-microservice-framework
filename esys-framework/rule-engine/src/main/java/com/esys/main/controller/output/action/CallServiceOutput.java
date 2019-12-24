package com.esys.main.controller.output.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallServiceOutput extends BaseActionOutput{

	private String body;
	private Map<String, List<String>> headers;
	private String status;
	private int statusCode;
	private Date time;
}
