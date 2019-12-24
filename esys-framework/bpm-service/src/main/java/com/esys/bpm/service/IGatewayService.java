package com.esys.bpm.service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.GatewayDto;

public interface IGatewayService {

	BpmBaseResult<?> validateGateway(GatewayDto gateway);

	BpmBaseResult<GatewayDto> saveGateway(GatewayDto gateway);

	BpmBaseResult<GatewayDto> findById(Long gatewayId);
}
