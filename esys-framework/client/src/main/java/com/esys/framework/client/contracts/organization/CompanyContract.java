package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

public interface CompanyContract {

    @RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<List<CompanyDto>> getAllCompanies() throws Exception;


    @RequestMapping(value = "/company",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<CompanyDto>> saveCompany(@Valid @RequestBody CompanyDto companyDto) throws Exception;

    @RequestMapping(value = "/company",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<CompanyDto>> updateCompany(@Valid @RequestBody CompanyDto companyDto) throws Exception;

    @RequestMapping(value = "/company",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult> deleteCompany(@Valid @RequestBody final long id) throws Exception;

    @RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<CompanyDto> getCompanyById(@Valid @RequestBody final long id) throws Exception;
}
