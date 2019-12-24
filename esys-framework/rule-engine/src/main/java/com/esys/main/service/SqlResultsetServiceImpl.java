package com.esys.main.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.main.controller.input.CreateSqlResultInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dao.PageDao;
import com.esys.main.dao.PageRuleDao;
import com.esys.main.dao.SqlResultsetDao;
import com.esys.main.dto.SqlResultSetDTO;
import com.esys.main.entity.RenginePage;
import com.esys.main.entity.RenginePageRule;
import com.esys.main.entity.RengineSqlResultSet;
import com.esys.main.utils.JwtTokenDataHolder;
import com.esys.main.utils.StringUtil;

@Service("sqlResultsetService")
public class SqlResultsetServiceImpl implements SqlResultsetService {
	
	
	@Autowired
	private SqlResultsetDao sqlResultsetDao;
	
	@Autowired
	private PageDao pageDao;
	
	@Autowired
	private PageRuleDao pageRuleDao;
	

	
	@Override
	public OutputDTO remove(String resultSetName) {
		OutputDTO output = new OutputDTO();
		
		RengineSqlResultSet sqlResultSet = sqlResultsetDao.getSqlResultSetByName(resultSetName);
		if(sqlResultSet == null) {
			output.addErrorMessage("sql bulunamadi");
			return output;
		}
		
		sqlResultsetDao.remove(sqlResultSet);                                                                                                                                                                                                                                             
		output.addSuccessMessage("Sql Deleted!...");
		return output;
	}
	
	@Override
	public OutputDTO<List<SqlResultSetDTO>> getsqlResultSetList(String pageName,String ruleName) {
		OutputDTO<List<SqlResultSetDTO>> output = new OutputDTO<List<SqlResultSetDTO>>();
		
		OutputDTO<Map<String,Long>> validIdDTO = generateValidId(pageName,ruleName);
		if(validIdDTO.isThereMessage()) {
			output.setMessages(validIdDTO.getMessages());
			return output;
		}
		Map<String,Long> idMap  = validIdDTO.getOutputData();
		Long pageId = idMap.get("pageId");
		Long pageRuleId = idMap.get("pageRuleId");
		
		List<RengineSqlResultSet> sqlResultSetList =  sqlResultsetDao.getSqlResultSetWithParameter(pageId,pageRuleId);
		output.setOutputData(SqlResultSetDTO.toDTOList(sqlResultSetList));
		output.addSuccessMessage("Sorgu Basarili");
		return output;
	}
	
	@Override
	public OutputDTO<SqlResultSetDTO> updateSqlResultSet(SqlResultSetDTO input) {
		OutputDTO<SqlResultSetDTO> output = new OutputDTO<SqlResultSetDTO>();
		RengineSqlResultSet sqlResultSet = sqlResultsetDao.getSqlResultSetBySqlId(input.getSqlId());
		if(sqlResultSet == null) {
			output.addErrorMessage("SQL bulunamadi");
			return output;
		}
		
		
		if(!input.getResultSetName().equals(sqlResultSet.getResultSetName())) {
			RengineSqlResultSet sqlResultset2 = sqlResultsetDao.getSqlResultSetByName(input.getResultSetName());
			if(sqlResultset2 != null) {
				output.addErrorMessage("Girilen isimde baska bir SQL bulunmakta!");
				return output;
			}
		}
		
		String pageName = input.getPageName();
		String ruleName = input.getRuleName();
		
		OutputDTO<Map<String,Long>> validIdDTO = generateValidId(pageName,ruleName);
		if(validIdDTO.isThereMessage()) {
			output.setMessages(validIdDTO.getMessages());
			return output;
		}
		
		Map<String,Long> idMap  = validIdDTO.getOutputData();
		Long pageId = idMap.get("pageId");
		Long pageRuleId = idMap.get("pageRuleId");
		sqlResultSet.setResultSetName(input.getResultSetName());
		sqlResultSet.setPageId(pageId);
		sqlResultSet.setPageRuleId(pageRuleId);
		sqlResultSet.setSql(input.getSql());
		sqlResultSet.setUpdateDate(new Date());
		sqlResultSet = sqlResultsetDao.saveOrUpdate(sqlResultSet); 

		output.addSuccessMessage("Sql Update Sucsess...");
		output.setOutputData(SqlResultSetDTO.toDTO(sqlResultSet));
		return output;
		
	}

	@Override
	public OutputDTO create(CreateSqlResultInput input) {	
		//sipring validasyonla alan kontrlu eklenecek
		OutputDTO<SqlResultSetDTO> output =new OutputDTO<SqlResultSetDTO>();
		RengineSqlResultSet sqlResultSet = sqlResultsetDao.getSqlResultSetByName(input.getResultSetName());
		if(sqlResultSet != null) {
			output.addErrorMessage("Bu resultSet name de baska bir kayit vardir!");
			return output;
		}
		
		String pageName = input.getPageName();	
		String ruleName = input.getRuleName();
		OutputDTO<Map<String,Long>> validIdDTO = generateValidId(pageName,ruleName);
		if(validIdDTO.isThereMessage()) {
			output.setMessages(validIdDTO.getMessages());
			return output;
		}
		Map<String,Long> idMap  = validIdDTO.getOutputData();
		Long pageId = idMap.get("pageId");
		Long pageRuleId = idMap.get("pageRuleId");
		String platform = JwtTokenDataHolder.getInstance().getPlatform();
		String sql = input.getSql();
		String resultSetName = input.getResultSetName();
		
		RengineSqlResultSet resultSet = new RengineSqlResultSet();
		resultSet.setCreateDate(new Date());
		resultSet.setPageId(pageId);
		resultSet.setPlatform(platform);
		resultSet.setPageRuleId(pageRuleId);
		resultSet.setResultSetName(resultSetName);
		resultSet.setSql(sql);
		sqlResultsetDao.saveOrUpdate(resultSet);
		
		output.addSuccessMessage("resultSet create!..");
		output.setOutputData(SqlResultSetDTO.toDTO(resultSet));
		
		return output;
	}

	private OutputDTO<Map<String,Long>> generateValidId(String pageName,String ruleName){
		Long pageId = null;
		Long pageRuleId = null;
		OutputDTO<Map<String, Long>> output = new OutputDTO<Map<String, Long>>();
		if(StringUtil.isNotEmpty(pageName)) {
			RenginePage page = pageDao.getPageByPageName(pageName);
			if(page == null) {
				output.addErrorMessage("Sayfa bulunamadı");
				return output;
			}else {
				pageId = page.getPageId();
			}
		}
		if(StringUtil.isNotEmpty(ruleName)) {
			if(pageId == null) {
				output.addErrorMessage("Geçerli Sayfa ismi girmelisiniz!..");
				return output;
			}else {
				RenginePageRule ruleEntity =  pageRuleDao.getPageRuleByRuleNameAndPageId(ruleName,pageId);	
				if(ruleEntity == null) {
					output.addErrorMessage("rule bulunamadi");
					return output;
				}else {
					pageRuleId = ruleEntity.getPageRuleId();
				}
			}
		}
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("pageId", pageId);
		map.put("pageRuleId", pageRuleId);
		output.setOutputData(map);
		
		return output;
		
	}

}
