package com.esys.main.controller.input;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoSaveInput {

	private String pageName;
	private String project;
	private List<FieldData> fieldDataList;
	private String platform;
}
