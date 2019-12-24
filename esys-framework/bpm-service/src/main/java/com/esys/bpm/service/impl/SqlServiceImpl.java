package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.SqlTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.entity.SqlTask;
import com.esys.bpm.enums.SqlType;
import com.esys.bpm.repository.SqlTaskRepository;
import com.esys.bpm.service.ISqlService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("sqlService")
public class SqlServiceImpl implements ISqlService {

	@Autowired
	private SqlTaskRepository sqlTaskRepository;

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		SqlTaskDto dto = (SqlTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.SQL_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);
		if (dto.getSqlType() == null)
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_SQL_TYPE);
		else if (dto.getSqlType().equals(SqlType.FUNCTION)
				&& StringUtil.isNullOrEmptyOrWhitespace(dto.getFunctionName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_SQL_FUNCTION);
		else if (dto.getSqlType().equals(SqlType.NATIVESQL) && StringUtil.isNullOrEmptyOrWhitespace(dto.getNativeSql()))
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_SQL_NATIVE);

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		SqlTaskDto dto = (SqlTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				SqlTask newEntity = dto.toEntity();
				SqlTask dbEntity = sqlTaskRepository.findById(newEntity.getSqlTaskId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());
				dbEntity.setSqlType(newEntity.getSqlType());
				dbEntity.setFunctionName(newEntity.getFunctionName());
				dbEntity.setNativeSql(newEntity.getNativeSql());

				// TODO değişenleri loglama

				dbEntity = sqlTaskRepository.save(dbEntity);
				result.setData(SqlTaskDto.toDto(dbEntity));
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> findById(Long taskId) {
		BpmBaseResult<TaskDto> result = new BpmBaseResult<TaskDto>();

		if (taskId == null)
			result.addErrorMessage(MessageText.NO_DATA_FOUND);
		else {
			try {
				Optional<SqlTask> entity = sqlTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(SqlTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}
}
