package com.esys.main.controller.input;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExecuteRuleEngineInput {

	private String pageName;
	private List<FieldData> fieldList;
	
}
