package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

public interface GroupContract {

    @RequestMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<List<GroupDto>> getAllGroups() throws Exception;


    @RequestMapping(value = "/groups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<GroupDto>> saveGroup(@Valid @RequestBody GroupDto groupDto) throws Exception;

    @RequestMapping(value = "/groups",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<GroupDto>> updateGroup(@Valid @RequestBody GroupDto groupDto) throws Exception;

    @RequestMapping(value = "/groups",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult> deleteGroup(@Valid @RequestBody final long id) throws Exception;

    @RequestMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<GroupDto> getGroupById(@Valid @RequestBody final long id) throws Exception;
}
