package com.esys.main.controller.input;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FieldData {

	private String fieldName;
	private String fieldValue;
	private String type;
	private String defaultValue;
	private List<ListData> listData;
	
}
