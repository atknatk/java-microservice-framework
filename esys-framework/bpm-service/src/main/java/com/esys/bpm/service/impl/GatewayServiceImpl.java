package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.GatewayDto;
import com.esys.bpm.entity.Gateway;
import com.esys.bpm.repository.GatewayRepository;
import com.esys.bpm.service.IGatewayService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("gatewayService")
public class GatewayServiceImpl implements IGatewayService {

	@Autowired
	private GatewayRepository gatewayRepository;

	@Override
	public BpmBaseResult validateGateway(GatewayDto gateway) {
		GatewayDto dto = gateway;

		BpmBaseResult result = new BpmBaseResult();

		if (dto.getType() == null)
			result.addErrorMessage(gateway.getXmlId(), MessageText.INVALID_GATEWAY_TYPE);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(gateway.getXmlId(), MessageText.GATEWAY_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(gateway.getXmlId(),
					MessageText.DESCRIPTION + " - " + MessageText.DESCRIPTION_EMPTY_WARNING);
		// TODO türe göre incoming outgoing kontrolü

		return result;
	}

	@Override
	public BpmBaseResult<GatewayDto> saveGateway(GatewayDto gateway) {
		GatewayDto dto = gateway;

		BpmBaseResult<GatewayDto> result = validateGateway(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				Gateway newEntity = dto.toEntity();
				Gateway dbEntity = gatewayRepository.findById(newEntity.getGatewayId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());
				dbEntity.setType(newEntity.getType());

				// TODO değişenleri loglama

				dbEntity = gatewayRepository.save(dbEntity);
				result.setData(GatewayDto.toDto(dbEntity));
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public BpmBaseResult<GatewayDto> findById(Long taskId) {
		BpmBaseResult<GatewayDto> result = new BpmBaseResult<GatewayDto>();

		if (taskId == null)
			result.addErrorMessage(MessageText.NO_DATA_FOUND);
		else {
			try {
				Optional<Gateway> entity = gatewayRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(GatewayDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}
}
