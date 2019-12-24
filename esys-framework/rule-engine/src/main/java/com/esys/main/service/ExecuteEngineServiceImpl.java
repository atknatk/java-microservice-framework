package com.esys.main.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.esys.main.controller.input.CallServiceInput;
import com.esys.main.controller.input.ExecuteRuleEngineInput;
import com.esys.main.controller.input.FieldData;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.action.BaseActionOutput;
import com.esys.main.controller.output.action.CallServiceOutput;
import com.esys.main.controller.output.action.MessageOutput;
import com.esys.main.dao.ActionDao;
import com.esys.main.dao.PageDao;
import com.esys.main.dao.SqlResultsetDao;
import com.esys.main.entity.RengineAction;
import com.esys.main.entity.RengineActionHeaders;
import com.esys.main.entity.RenginePage;
import com.esys.main.entity.RenginePageRule;
import com.esys.main.entity.RengineSqlResultSet;
import com.esys.main.enums.ActionTypeEnum;
import com.esys.main.enums.SeverityEnum;
import com.esys.main.utils.DateUtil;

import bsh.Interpreter;
import lombok.extern.slf4j.Slf4j;

@Service("executeEngineService")
@Slf4j
public class ExecuteEngineServiceImpl implements ExecuteEngineService {
	
	
	@Autowired
	private PageDao pageDao;
	
	@Autowired
	private CallService callService;
	
	@Autowired
	private ActionDao actionDao;
	
	@Autowired
	private SqlResultsetDao sqlResultsetDao;
	
	@Autowired
	private DBOperationService dBOperationService;

	@Override
	public OutputDTO<BaseActionOutput> executeRuleEngine(ExecuteRuleEngineInput input) {
		

		//Spring validasyonla tum inputlar kontrol edilecek
		//validasyonu kontrol ederken ayni isimde iki fieldname olmamali kontrol et
		OutputDTO<BaseActionOutput> output = new OutputDTO<BaseActionOutput>();
		
		String pageName = input.getPageName();
		RenginePage page = pageDao.getPageByPageName(pageName);
		if(page == null) {
			output.addErrorMessage("Sayfa bulunamadi");
			return output;
		}
		
		List<RenginePageRule> pageRuleList = page.getRuleList();
		if(CollectionUtils.isEmpty(pageRuleList)) {
			output.addErrorMessage("Sayfada tanımli rule bulunamadi");
			return output;
		}
		
		//Sayfadaki rulelar sırasi ile calistirilip aksiyon varsa return edilecek
		for(RenginePageRule pageRule  :pageRuleList) {
			BaseActionOutput action = executeRule(input.getFieldList(),pageRule);
			if(action != null) {
				output.addSuccessMessage("Kural sonucu bir adet action uretildi");
				output.setOutputData(action);
				return output;
			}
		}
		
		output.addWarningMessage("kural dizisi boyunca action uretilemedi");
		return output;
	}

	private BaseActionOutput executeRule(List<FieldData> fieldDataList, RenginePageRule pageRule) {
		String ruleText = pageRule.getRuleText();
		HashMap<String,Object> varableMap = new HashMap<String,Object>();
		Long pageRuleId = pageRule.getPageRuleId();
		Long pageId = pageRule.getPageId();

		//kuralda kullanilan fonksiyonlar
		Map<String, Object> functionVarableMap = getFunctionVarableMap(ruleText);
		ruleText =(String) functionVarableMap.get("ruleText");
		Map functionParameterMap =(Map) functionVarableMap.get("parameterMap");
		if(!functionParameterMap.isEmpty()) {
			varableMap.putAll(functionParameterMap);
		}
		//kuralda kullanilan fieldler bulunuyor
		Map<String, Object> fieldDataVarableMap = getFieldDataVarableMap(ruleText, fieldDataList);
		ruleText =(String) fieldDataVarableMap.get("ruleText");
		Map fieldDataParameterMap =(Map) fieldDataVarableMap.get("parameterMap");
		if(!fieldDataParameterMap.isEmpty()) {
			varableMap.putAll(fieldDataParameterMap);
		}
		
		//kuralda kullanilan sql bulunuyor
		Map<String, Object> resultSetDataMap = findAndReplaceSQL(ruleText,varableMap);
		ruleText =(String) resultSetDataMap.get("ruleText");
		Map resultSetDataParamaterMap =(Map) resultSetDataMap.get("parameterMap");
		if(!resultSetDataParamaterMap.isEmpty()) {
			varableMap.putAll(resultSetDataParamaterMap);
		}
				
		//kuralda kullanilan action bulunuyor
		ruleText = findAndReplaceAction(ruleText);
		
		Object action = null;
		Object[] obj = null;
		List sqlList = null;
		try {
			Interpreter i = new Interpreter();
			i.setStrictJava(true);
			i.setShowResults(true);
			for(Map.Entry<String, Object> entrySet : varableMap.entrySet()) {
				i.set(entrySet.getKey(), entrySet.getValue());
			}		
			i.set("action", null);         
			i.eval(ruleText);
			action =  i.get("action");
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		if(action != null) {
			//gelen action degeri integer deger ise action id dir
			if(action instanceof Integer) {
				Long actionId = new Long((Integer)action);
				RengineAction actionEntity = actionDao.getActionByActionId(actionId);
				if(actionEntity != null) {
					String actionType = actionEntity.getActionType();
					if(actionType.equals(ActionTypeEnum.CALL_SERVICE.value())) {
						return callActionService(actionEntity);
					}else if(actionType.equals(ActionTypeEnum.MESSAGE.value())) {
						return callActionMessage(actionEntity,fieldDataParameterMap);
					}
				}else {
					MessageOutput message = new MessageOutput();
					message.setSeverity(SeverityEnum.INFO.value());
					message.setMessageText(""+actionId);
					return message;
				}
			}else if(action instanceof String) {
				//String bir deger ise direk mesaj verilmek istenmistir
				MessageOutput message = new MessageOutput();
				message.setSeverity(SeverityEnum.INFO.value());
				message.setMessageText((String)action);
				return message;
			}	
		}
		return null;		
	}

	private Map<String,Object> findAndReplaceSQL(String ruleText,HashMap<String,Object> fieldDataMap) {
		HashMap<String,Object> sqlResultSetDataMap = new HashMap<String, Object>();
		String[] sqlArr =  StringUtils.substringsBetween(ruleText, "SQL[", "]");
		if(sqlArr != null) {
			for(String sqlName : sqlArr) {
				String resultSetName = "";
				LinkedList<Object> sqlParameterList = new LinkedList<Object>();
				String[] parameterArr =  StringUtils.substringsBetween(sqlName, "(", ")");
				if(parameterArr != null) {
					List<String> parameterDataList = new ArrayList<String>(Arrays.asList(parameterArr[0].split(",")));
					for(String parameterData : parameterDataList) {
						Object parameterValue = fieldDataMap.get(parameterData);
						if(parameterValue == null) {
							//Eger fieldData degeri degilse kendisi value dir
							parameterValue = parameterData;
							if(StringUtils.isNumeric(parameterData)) {
								parameterValue = new BigDecimal(parameterData);
							}
						}	
						sqlParameterList.add(parameterValue);
					}
					resultSetName =  StringUtils.substringBefore(sqlName, "(");
				}else {
					resultSetName = sqlName;
				}
				RengineSqlResultSet resultSet = sqlResultsetDao.getSqlResultSetByName(resultSetName);
				List<Object[]> resultSetData = executeSQl(resultSet,sqlParameterList);
				sqlResultSetDataMap.put(resultSet.getResultSetName(), resultSetData);
				ruleText = StringUtils.replace(ruleText, "SQL["+sqlName+"]", ""+resultSet.getResultSetName());
			}
		}
		return returnResult(ruleText,sqlResultSetDataMap);
	}

	private List<Object[]> executeSQl(RengineSqlResultSet resultSet,LinkedList<Object> parameterList) {
		
		List<Object[]>  sqlData = dBOperationService.executeSql(resultSet.getSql(),parameterList);
		return sqlData;
	}

	private Map returnResult(String ruleText,Map parameterMap) {
		Map result = new HashMap<String,Object>();
		result.put("parameterMap", parameterMap);
		result.put("ruleText", ruleText);
		return result;
	}
	private Map<String,Object> getFunctionVarableMap(String ruleText) {
		HashMap<String,Object> functionValueMap = new HashMap<String, Object>();
		ruleText = setDateFunction(ruleText,functionValueMap);
		
		return returnResult(ruleText,functionValueMap);
	}
	
	

	private String setDateFunction(String ruleText,HashMap<String, Object> functionValueMap) {
		String[] dateArr =  StringUtils.substringsBetween(ruleText, "to_date(", ")");
		if(dateArr != null) {
			int dateIndex = 0;
			for(String dateStr : dateArr) {
				Date date =  DateUtil.parseDate(dateStr, "dd.MM.yyyy HH:mm:ss");
				String dateKey = "date_"+dateIndex;
				ruleText = StringUtils.replace(ruleText, "to_date("+dateStr+")", dateKey);
				functionValueMap.put(dateKey, date);
			}
		}
		
		return ruleText;
	}
	
	private Map<String,Object> getFieldDataVarableMap(String ruleText,List<FieldData> fieldDataList) {
		HashMap<String,Object> fieldDataValueMap = new HashMap<String, Object>();
		String[] filedArr =  StringUtils.substringsBetween(ruleText, "VAR[", "]");
		if(filedArr != null) {
			for(String filedName : filedArr) {
				ruleText = prepradFiledDataList(ruleText,filedName,fieldDataList,fieldDataValueMap);
			}
		}
		return returnResult(ruleText,fieldDataValueMap);
	}
	
	private String prepradFiledDataList(String ruleText,String filedName,List<FieldData> fieldDataList,HashMap<String,Object> fieldDataValueMap) {
		for(FieldData fieldData :fieldDataList) {
			if(filedName.equals(fieldData.getFieldName())) {
				ruleText = StringUtils.replace(ruleText, "VAR["+filedName+"]", filedName);
				if(fieldData.getType().equals("String")) {
					fieldDataValueMap.put(fieldData.getFieldName(),fieldData.getFieldValue());
				}else if(fieldData.getType().equals("Numeric")) {
					BigDecimal fieldValue = new BigDecimal(fieldData.getFieldValue());
					fieldDataValueMap.put(fieldData.getFieldName(), fieldValue);
				}else if(fieldData.getType().equals("Date")) {
					String format = "dd.MM.yyyy";
					if(fieldData.getFieldValue().length() > 10) {
						format = "dd.MM.yyyy HH:mm:ss";
					}
					Date fieldValue = DateUtil.parseDate(fieldData.getFieldValue(), format);
					fieldDataValueMap.put(fieldData.getFieldName(), fieldValue);
				}else {
					fieldDataValueMap.put(fieldData.getFieldName(), fieldData.getFieldValue());
				}
				
			}
		}
		
		return ruleText;
	}
	private String findAndReplaceAction(String ruleText) {
		String[] actionArr =  StringUtils.substringsBetween(ruleText, "AC[", "]");
		if(actionArr != null) {
			for(String actionName : actionArr) {
				RengineAction action = actionDao.getActionByActionName(actionName);
				ruleText = StringUtils.replace(ruleText, "AC["+actionName+"]", ""+action.getActionId());
			}
		}
		return ruleText;
	}

	private MessageOutput callActionMessage(RengineAction action,Map<String,Object> fieldDataVarableMap) {
		String actionBody = findAndReplaceParameterActionBody(action.getActionBody(),fieldDataVarableMap);
		
		MessageOutput output = new MessageOutput();
		output.setMessageText(actionBody);
		output.setSeverity(action.getSeverity());
		output.setTimestamp(new Date());
		output.setType(action.getActionType());
		return output;
	}
	
	private String findAndReplaceParameterActionBody(String actionBody,Map<String,Object> parameterMap) {
		String[] actionArr =  StringUtils.substringsBetween(actionBody, "VAR[", "]");
		if(actionArr != null) {
			for(String actionName : actionArr) {
				Object parameterValue = parameterMap.get(actionName);
				if(parameterValue instanceof Date) {
					parameterValue = DateUtil.formatDDMMYYYY((Date)parameterValue);
				}
				actionBody = StringUtils.replace(actionBody, "VAR["+actionName+"]",""+ parameterValue);
			}
		}
		return actionBody;
	}
	
	private BaseActionOutput callActionService(RengineAction action) {
		CallServiceInput input = new CallServiceInput();
		input.setBody(action.getActionBody());
		input.setContentType(action.getContentType());
		
		List<RengineActionHeaders> actionHeaders = action.getHeaders();
		Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
		for(RengineActionHeaders headersEntity : actionHeaders) {
			String headerName = headersEntity.getHeaderName();
			String headerValue = headersEntity.getHeaderValue();
			String[] headersArr = StringUtils.split(headerValue, ";");
			if(headersArr != null) {
				List<String> headersList = CollectionUtils.arrayToList(headersArr);
				requestHeaders.put(headerName, headersList);
			}
			
			
		}
		input.setHeaders(requestHeaders);
		input.setHttpMethod(action.getHttpMethod());
		input.setUrl(action.getUrl());
		
 		OutputDTO<CallServiceOutput> callServiceOutput = callService.callRest(input);
		return callServiceOutput.getOutputData();
	}

}
