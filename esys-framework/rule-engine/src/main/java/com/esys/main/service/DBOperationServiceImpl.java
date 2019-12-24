package com.esys.main.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.esys.main.controller.input.routine.ExecuteRoutineInput;
import com.esys.main.controller.input.routine.RoutineParameterInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.routine.RoutineDataOutput;
import com.esys.main.controller.output.routine.RoutineParameterOutput;
import com.esys.main.enums.ParameterModeEnum;
import com.esys.main.enums.PostgreSqlTypeMappingEnum;

@Service("dBOperationService")
public class DBOperationServiceImpl implements DBOperationService {
	

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
	JdbcTemplate jdbcTemplate;
	
    @Override
	public OutputDTO<List<Map<String, Object>>> executeRoutine(ExecuteRoutineInput input) {
		
    	OutputDTO<List<Map<String, Object>>> output = new OutputDTO<List<Map<String, Object>>>();
		
    	String routineName = input.getRoutineName();
    	String specificSchema = input.getSpecificSchema();
    	List<Object[]> routineDataList = getRoutineDataListByShemaAndRoutineName(routineName,specificSchema);
    	List<RoutineDataOutput> routineList = generateRoutineData(routineDataList);
    	if(!CollectionUtils.isEmpty(routineList)) {
    		RoutineDataOutput routine =routineList.get(0);
    		
    		
    		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
    		
    	  if(routine.getRoutineType().equals("FUNCTION")) {
    		  jdbcCall.withFunctionName(routineName);
    	  }else if(routine.getRoutineType().equals("PROCEDURE")) {
    		  jdbcCall.withProcedureName(routineName);
    	  }
    	  jdbcCall.withoutProcedureColumnMetaDataAccess();
    	    
    	    List<SqlParameter> sqlParameterList = new LinkedList<SqlParameter>();
    	    
    	    if(!isThereOutParameterInList(routine.getRoutineParameters())
    				&& routine.getReturnDataType().equals("refcursor")) {
    			sqlParameterList.add(new SqlOutParameter("cur", Types.REF_CURSOR));
    		}
    	    
    		for(RoutineParameterOutput routineParameter : routine.getRoutineParameters()) {
    			SqlParameter sqlParameter = null;
    			if(routineParameter.getParameterMode().equals("OUT")) {
    				sqlParameter = new SqlOutParameter(routineParameter.getParameterName(), convertSQLType(routineParameter.getParameterDataType()));
    			}else if(routineParameter.getParameterMode().equals("INOUT")) {
    				sqlParameter = new SqlInOutParameter(routineParameter.getParameterName(), convertSQLType(routineParameter.getParameterDataType()));
    			}else if(routineParameter.getParameterMode().equals("IN")) {
    				sqlParameter = new SqlParameter(routineParameter.getParameterName(), convertSQLType(routineParameter.getParameterDataType()));
    			}
    			sqlParameterList.add(sqlParameter);
    		}
    		
    	    
    		SqlParameter[] sqlParameterArray = new SqlParameter[sqlParameterList.size()];
    		sqlParameterArray = sqlParameterList.toArray(sqlParameterArray);
			jdbcCall.declareParameters(sqlParameterArray);
			
    	    if(!isThereOutParameterInList(routine.getRoutineParameters())
    				&& routine.getReturnDataType().equals("refcursor")) {
    	    	
    	    	jdbcCall.returningResultSet("cur", new RowMapper<List<Map<String,Object>>>() {

					@Override
					public List<Map<String,Object>> mapRow(ResultSet rs, int rowNum) throws SQLException {
						List<Map<String,Object>> output = resultSetToArrayList(rs);
						return output;
					}

				});
            }
    	 
        	SqlParameterSource inParams = new MapSqlParameterSource();
        			
    
             	  
            for(RoutineParameterInput parameter : input.getParameters()) {
            	
            	Object parameterValue = getRoutineParamete(parameter.getParameterName(),input.getParameters());
            	
            	
            	if(parameterValue == null) {
            		output.setOutputData(null);
        			output.addErrorMessage("Parameter name bulunamadi");
        			return output;
            	}
            	
            	((MapSqlParameterSource) inParams).addValue(parameter.getParameterName(), parameterValue);
        	
//    		Map result = createUserProc.execute(inParams);
//    	    query.registerStoredProcedureParameter(index,
//    					convertSQLTypeToJavaType(parameter.getParameterDataType()), 
//    					convertParameterMode(parameter.getParameterMode()))
//    						.setParameter(index,getRoutineParamete(parameter.getParameterName(),input.getParameters()));
//    			
//    			index++;
    		}
    		
 
    		
            Map<String, Object> outProcedureData = jdbcCall.execute(inParams);
            List<Map<String,Object>> outputList = new ArrayList<>(); 
            if(!isThereOutParameterInList(routine.getRoutineParameters())
    				&& routine.getReturnDataType().equals("refcursor")) { 
            	outputList = (List<Map<String,Object>>) outProcedureData.get("cur");
            }else {
            	outputList.add(outProcedureData);
            }
    	    	
            
           
    		output.setOutputData(outputList);
			output.addSuccessMessage("Sorgu pasarılı");
			return output;
    		
    	}
		
    	output.setOutputData(null);
		output.addErrorMessage("sonuc bulunamadı");
		return output;
	}
	
    public List<Map<String,Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
    	  ResultSetMetaData md = rs.getMetaData();
    	  int columns = md.getColumnCount();
    	  List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	  Map<String,Object> row = new HashMap<String,Object>(columns);
 	      for(int i=1; i<=columns; ++i){           
 	        row.put(md.getColumnName(i),rs.getObject(i));
 	      }
 	      list.add(row);

    	 return list;
    	}
    
    private Object getRoutineParamete(String parameterName, List<RoutineParameterInput> parameters) {
		for(RoutineParameterInput inputParameter:parameters) {
			if(inputParameter.getParameterName().equals(parameterName)) {
				return inputParameter.getParameterValue();
			}
		}
		return null;
	}

	private int convertSQLType(String parameterDataType) {
		PostgreSqlTypeMappingEnum mappingEnum = PostgreSqlTypeMappingEnum.fromPostgreType(parameterDataType);
    	return mappingEnum.sqlType();
    }
    
    private ParameterMode convertParameterMode(String parameterMode) {
    	ParameterModeEnum parameterModeEnum =  ParameterModeEnum.fromParameterMode(parameterMode);
    	return parameterModeEnum.value();
    }
    
	private boolean isThereOutParameterInList(List<RoutineParameterOutput> routineParameters) {
		for(RoutineParameterOutput parameter : routineParameters) {	
			if(parameter.getParameterMode().equals("OUT")) {
    			return true;
			}		
		}
		return false;
	}

	@Override
	public OutputDTO<List<RoutineDataOutput>> getRoutineData(String specificSchema) {
		OutputDTO<List<RoutineDataOutput>> output = new OutputDTO<List<RoutineDataOutput>>();
		
		List<Object[]> routineDataList = getRoutineDataListByShema(specificSchema);
		
		
		if(!CollectionUtils.isEmpty(routineDataList)) {		
			List<RoutineDataOutput> returnList = generateRoutineData(routineDataList);
			output.setOutputData(returnList);
			output.addSuccessMessage("Sorgu pasarılı");
			return output;
		}
		
		output.setOutputData(null);
		output.addErrorMessage("Foksiyon ve procedure bulunamadı");
		
		return output;
	}
	
	private List<Object[]> getRoutineDataListByShema(String specificSchema) {
		LinkedList<Object> parameterList = new LinkedList<Object>();
		parameterList.add(specificSchema);
		List<Object[]> routineDataList = executeSql("select routines.routine_name,"
				+ "						   routines.routine_type,"
				+ "						   routines.data_type as return_data_type,"
				+ "						   parameters.data_type as parameter_data_type,"
				+ "						   parameters.ordinal_position,"
				+ "						   parameters.parameter_mode,"
				+ "						   parameters.parameter_name"
				+ "					FROM information_schema.routines" 
				+ "    LEFT JOIN information_schema.parameters ON routines.specific_name=parameters.specific_name" 
				+ "         WHERE routines.specific_schema=?" 
				+"     ORDER BY routines.routine_name, parameters.ordinal_position",parameterList);
		
		return routineDataList;
		
	}
	
	private List<Object[]> getRoutineDataListByShemaAndRoutineName(String specificSchema,String routineName) {
		LinkedList<Object> parameterList = new LinkedList<Object>();
		parameterList.add(routineName);
		parameterList.add(specificSchema);
		List<Object[]> routineDataList = executeSql("select routines.routine_name,"
				+ "						   routines.routine_type,"
				+ "						   routines.data_type as return_data_type,"
				+ "						   parameters.data_type as parameter_data_type,"
				+ "						   parameters.ordinal_position,"
				+ "						   parameters.parameter_mode,"
				+ "						   parameters.parameter_name"
				+ "					FROM information_schema.routines" 
				+ "    LEFT JOIN information_schema.parameters ON routines.specific_name=parameters.specific_name" 
				+ "         WHERE routines.specific_schema=?" 
				+ "           AND routines.routine_name = ?"
				+"     ORDER BY routines.routine_name, parameters.ordinal_position",parameterList);
		
		return routineDataList;
		
	}

	private RoutineDataOutput isThereRoutineInReturnList(String routineName, List<RoutineDataOutput> returnList) {
		for(RoutineDataOutput data : returnList) {
			if(data.getRoutineName().equals(routineName)) {
				return data;
			}
		}
		return null;
	}
	
	private List<RoutineDataOutput> generateRoutineData(List<Object[]> routineDataList) {
		List<RoutineDataOutput> returnList = new ArrayList<RoutineDataOutput>();
		for(Object[] routineData : routineDataList) {
			
			String routineName = (String) routineData[0];
			String routineType = (String) routineData[1];
			String returnDataType = (String) routineData[2];
			
			RoutineDataOutput routine = new RoutineDataOutput();
			routine.setRoutineName(routineName);
			routine.setReturnDataType(returnDataType);
			routine.setRoutineType(routineType);
			
			
			String parameterDataType = (String) routineData[3];
			int ordinal = (int) routineData[4];
			String parameterMode = (String) routineData[5];
			String parameterName = (String) routineData[6];
			
			RoutineParameterOutput parameter = new RoutineParameterOutput();
			parameter.setOrdinal(ordinal);
			parameter.setParameterDataType(parameterDataType);
			parameter.setParameterMode(parameterMode);
			parameter.setParameterName(parameterName);
			
			RoutineDataOutput routineSame = isThereRoutineInReturnList(routineName,returnList);
			if(routineSame == null) {
				List<RoutineParameterOutput> routineParameters = new ArrayList<RoutineParameterOutput>();
				routineParameters.add(parameter);
				routine.setRoutineParameters(routineParameters);
				
				returnList.add(routine);
			}else {
				List<RoutineParameterOutput> routineSameParameters = routineSame.getRoutineParameters();
				routineSameParameters.add(parameter);
				routineSame.setRoutineParameters(routineSameParameters);
			}
			
		}
		return returnList;
	}

	@Override
	public List<Object[]> executeSql(String sqlValue) {	
		return executeSql(sqlValue,null);	
	}
	
	@Override
	public List<Object[]> executeSql(String sqlValue,LinkedList<Object> parameterList) {

		Query q = entityManager.createNativeQuery(sqlValue);
		if(!CollectionUtils.isEmpty(parameterList)) {
			int index = 1;
			for(Object parameterValue : parameterList) {
				q.setParameter(index++, parameterValue);
			}	
		}
		List author = q.getResultList();
		return author;
	}
	

	

	


	

}
