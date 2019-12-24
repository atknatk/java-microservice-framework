package com.esys.main.dao;

import java.util.List;

import com.esys.main.entity.RengineSqlResultSet;

public interface SqlResultsetDao {

	RengineSqlResultSet saveOrUpdate(RengineSqlResultSet rengineSqlResultset);

	void remove(RengineSqlResultSet rengineSqlResultset);

	RengineSqlResultSet getSqlResultSetByName(String resultSetName);

	List<RengineSqlResultSet> getSqlResultSetWithParameter(Long pageId, Long pageRuleId);

	RengineSqlResultSet getSqlResultSetBySqlId(Long sqlId);

}
