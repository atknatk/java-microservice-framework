package com.esys.framework.organization.web.controller;

import com.esys.framework.client.contracts.organization.CompanyContract;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IBranchOfficeService;
import com.esys.framework.organization.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("company")
public class CompanyController implements CompanyContract {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IBranchOfficeService branchOfficeService;


    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResult<CompanyDto>> saveCompany(@Valid @RequestBody CompanyDto companyDto) throws Exception {
        log.debug("REST request to save Company : {}", companyDto);
        ModelResult<CompanyDto> result =  companyService.save(companyDto);
        return ResponseEntity.created(new URI("/api/company/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<CompanyDto>> updateCompany(@Valid @RequestBody CompanyDto companyDto) throws Exception {
        ModelResult<CompanyDto> result =  companyService.update(companyDto);
        return ResponseEntity.created(new URI("/api/company/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteCompany(@Valid  @PathVariable("id")  long id) throws Exception {
        ModelResult result =  companyService.delete(id);
        return ResponseEntity.created(new URI("/api/company/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<CompanyDto> getCompanyById(@Valid  @PathVariable("id")  long id) throws Exception {
        ModelResult<CompanyDto> result = companyService.findOne(id);
        return result;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<CompanyDto>> getAllCompanies() throws Exception {

        ModelResult<List<CompanyDto>> list = companyService.getAll();
        return list;
    }

    @GetMapping(path = "/{id}/branchoffice",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<BranchOfficeDto>> findBranchOfficesByCompany(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<List<BranchOfficeDto>> result =  branchOfficeService.findBranchOfficesByCompany(id);
        return result;
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return companyService.users(id);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                    @RequestBody List<User> users) {
        companyService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        companyService.delete(id, new User(userId));
        return ResponseEntity.ok(null);
    }
}
