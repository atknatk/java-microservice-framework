package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

public interface MainGroupContract {

    @RequestMapping(value = "/maingroups", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<List<MainGroupDto>> getAllMainGroups() throws Exception;


    @RequestMapping(value = "/maingroups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<MainGroupDto>> saveMainGroup(@Valid @RequestBody MainGroupDto mainGroupDto) throws Exception;

//    @RequestMapping(value = "/maingroups",
//            method = RequestMethod.PUT,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<ModelResult<MainGroupDto>> updateMainGroup(@Valid @RequestBody MainGroupDto mainGroupDto) throws Exception;

    @RequestMapping(value = "/maingroups",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult> deleteMainGroup(@Valid @RequestBody final long id) throws Exception;

    @RequestMapping(value = "/maingroups", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<MainGroupDto> getMainGroupById(@Valid @RequestBody final long id) throws Exception;
}
