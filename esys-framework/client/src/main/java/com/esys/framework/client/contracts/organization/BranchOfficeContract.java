package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

public interface BranchOfficeContract {

    ModelResult<List<BranchOfficeDto>> getAllBranchOffices() throws Exception;
    ResponseEntity<ModelResult<BranchOfficeDto>> saveBranchOffice(@Valid @RequestBody BranchOfficeDto branchOfficeDto) throws Exception;
    ResponseEntity<ModelResult<BranchOfficeDto>> updateBranchOffice(@Valid @RequestBody BranchOfficeDto branchOfficeDto) throws Exception;
    ResponseEntity<ModelResult> deleteBranchOffice(@Valid @RequestBody final long id) throws Exception;
    ModelResult<BranchOfficeDto> getBranchOfficeById(@Valid @RequestBody final long id) throws Exception;
}
