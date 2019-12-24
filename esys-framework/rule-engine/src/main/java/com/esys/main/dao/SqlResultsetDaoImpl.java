package com.esys.main.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.esys.main.entity.RengineSqlResultSet;
import com.esys.main.repository.SqlResultsetRepository;
import com.esys.main.utils.JwtTokenDataHolder;

@Repository
public class SqlResultsetDaoImpl implements SqlResultsetDao {
	
	@Autowired
	private SqlResultsetRepository sqlResultsetRepository;
	
	public RengineSqlResultSet saveOrUpdate(RengineSqlResultSet rengineSqlResultset) {
		return sqlResultsetRepository.save(rengineSqlResultset);
	}
	
	public void remove(RengineSqlResultSet rengineSqlResultset) {
		sqlResultsetRepository.delete(rengineSqlResultset);
	}
	
	@Override
	public List<RengineSqlResultSet> getSqlResultSetWithParameter(Long pageId,Long pageRuleId) {
		return sqlResultsetRepository.getSqlResultSet(pageId,pageRuleId,JwtTokenDataHolder.getInstance().getPlatform());
	}

	@Override
	public RengineSqlResultSet getSqlResultSetByName(String resultSetName) {
		return sqlResultsetRepository.findByResultSetNameAndPlatform(resultSetName,JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public RengineSqlResultSet getSqlResultSetBySqlId(Long sqlId) {
		Optional<RengineSqlResultSet> result = sqlResultsetRepository.findById(sqlId);
		if(result.isPresent()) {
			return result.get();
		}
		return null;
	}
}