package com.esys.framework.client.service.organization;

import com.esys.framework.client.contracts.organization.BranchOfficeContract;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "organization-service")
public interface BranchOfficeClient extends BranchOfficeContract {
}
