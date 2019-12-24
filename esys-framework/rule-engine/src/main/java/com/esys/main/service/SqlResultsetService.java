package com.esys.main.service;

import java.util.List;

import com.esys.main.controller.input.CreateSqlResultInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.SqlResultSetDTO;

public interface SqlResultsetService {

	OutputDTO create(CreateSqlResultInput input);

	OutputDTO remove(String resultSetName);

	OutputDTO<List<SqlResultSetDTO>> getsqlResultSetList(String pageName, String ruleName);

	OutputDTO<SqlResultSetDTO> updateSqlResultSet(SqlResultSetDTO input);
	
}
