package com.esys.main.controller.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSqlResultInput {

	private String pageName;
	private String ruleName;
	private String sql;
	private String resultSetName;
	
}
