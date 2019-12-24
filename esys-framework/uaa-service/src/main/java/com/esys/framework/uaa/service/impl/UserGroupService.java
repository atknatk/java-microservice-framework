package com.esys.framework.uaa.service.impl;

import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import com.esys.framework.core.exceptions.AllreadyExistsException;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.uaa.repository.IUserGroupRepository;
import com.esys.framework.uaa.service.IUserGroupService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserGroupService implements IUserGroupService{

    @Autowired
    private IUserGroupRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserGroupDto saveUserGroup(UserGroupDto userGroupDto) {
        boolean isExist = repository.existsByName(userGroupDto.getName());

        if(isExist){
            throw new AllreadyExistsException();
        }
        final UserGroup saved = repository.save(toEntity(userGroupDto));
        log.info("UserGroup was saved {} ", userGroupDto);
        return toDto(saved);
    }

    @Override
    public UserGroup saveUserGroupIfNotExist(UserGroup userGroup) {
        Optional<UserGroup> group = repository.getUserGroupByName(userGroup.getName());

        if(group.isPresent()){
            return group.get();
        }
        UserGroup saved =  repository.save(new UserGroup(userGroup.getName()));
        log.info("UserGroup was saved {} ", saved);
        return  saved;
    }


    @Override
    public void assignUsers(Long id, List<UserDto> list) {
        Optional<UserGroup> groupOptional = repository.findByIdWithUser(id);
        if(!groupOptional.isPresent()){
            return;
        }
        UserGroup group = groupOptional.get();

        if(!Hibernate.isInitialized(group.getUsers())){
            Hibernate.initialize(group.getUsers());
        }

        group.setUsers(new ArrayList<>());

        list.forEach(l -> group.getUsers().add(new User(l)));
        UserGroup saved =  repository.save(group);
        log.info("UserGroup was assigned {} ", saved);
    }

    @Override
    public UserGroupDto saveUserGroupIfNotExist(UserGroupDto userGroupDto) {
        return toDto(saveUserGroupIfNotExist(toEntity(userGroupDto)));
    }

    @Override
    public void deleteUserGroup(Long id) {
        repository.deleteById(id);
        log.info("UserGroup was saved old id : {} ", id);
    }

    @Override
    public Optional<UserGroupDto> findOne(Long id) {
        log.debug("Getting UserGroup by id. id : {}", id);
        Optional<UserGroup> group = repository.findById(id);
        if(!group.isPresent()){
            return Optional.empty();
        }
        final UserGroupDto userGroupDto =  toDto(group.get());
        log.debug("Fetched UserGroup by id. id : {}", id);
        return Optional.of(userGroupDto);
    }

    @Override
    public List<UserGroupDto> findAll() {
        log.debug("Finding all userGroup ");
        List<UserGroup> all = repository.findAll();
        log.debug("Converting all userGroup ");
        List<UserGroupDto> data=  all.stream()
                .map(one -> mapper.map(one, UserGroupDto.class))
                .collect(Collectors.toList());
        log.info("Converted all userGroup ");
        log.info("Fetched all userGroup");
        return data;
    }

    @Override
    public List<UserGroupDto> findAllByUser(Long userId) {
        log.debug("Finding all userGroup by user : " + userId);
        Collection<User> users = new ArrayList<>();
        users.add(new User(userId));
        List<UserGroup> all = repository.getUserGroupByUsers(users);
        log.debug("Converting all userGroup by user : " + userId);
        List<UserGroupDto> data=  all.stream()
                .map(one -> mapper.map(one, UserGroupDto.class))
                .collect(Collectors.toList());
        log.info("Converted all userGroup by user : " + userId);
        log.info("Fetched all userGroup by user : " + userId);
        return data;
    }

    @Override
    public ModelResult setEnabled(Long id, boolean enabled) {
        log.debug("Finding usergroup userGroup ");
        Optional<UserGroup> userGroup = repository.findById(id);
        if(userGroup.isPresent()){
            UserGroup entity = userGroup.get();
            entity.setEnabled(enabled);
            repository.save(entity);
            return new ModelResult.ModelResultBuilders().success();
        }else{
            return new ModelResult.ModelResultBuilders()
                    .setStatus(ResultStatusCode.DATA_NOT_FOUND)
                    .build();
        }
    }

    @Override
    public boolean exist(Long id) {
        return repository.existsById(id);
    }

    @Override
    public DataTablesOutput<UserGroupDto> paging(DataTablesInput input) {
        Function<UserGroup, UserGroupDto> toDto = entity -> toDto(entity);
        return repository.findAll(input,toDto);
    }

    @Override
    public List<UserDto> findUserByUserGroup(long longValue) {
        final Optional<UserGroup> byId = repository.findByIdWithUser(new Long(longValue));
        if(!byId.isPresent()) return  new ArrayList<>();
        return mapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    private UserGroup toEntity(UserGroupDto dto){
        log.debug("UserGroupDto converting to UserGroup {}",dto);
        final UserGroup converted = mapper.map(dto, UserGroup.class);
        log.debug("UserGroupDto converted to UserGroup {}",converted);
        return converted;
    }

    private UserGroupDto toDto(UserGroup userGroup){
        log.debug("UserGroup converting to UserGroupDto {}",userGroup);
        final UserGroupDto converted = mapper.map(userGroup, UserGroupDto.class);
        log.debug("UserGroup converted to UserGroupDto {}",converted);
        return converted;
    }

}
