package com.esys.framework.uaa.service.impl;

import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.exceptions.ResourceNotFoundException;
import com.esys.framework.core.service.impl.BaseService;
import com.esys.framework.uaa.repository.IRoleRepository;
import com.esys.framework.uaa.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@Transactional
public class RoleService extends BaseService<RoleDto,Role> implements IRoleService {

    private transient final IRoleRepository roleRepository;



    private transient final ModelMapper modelMapper;

    @Autowired
    public RoleService(ModelMapper mapper, IRoleRepository roleRepository) {
        super(mapper, log, RoleDto.class, Role.class, roleRepository);
        this.modelMapper = mapper;
        this.roleRepository = roleRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('authority.role.new','authority.role.edit','authority.role.delete')")
    public List<RoleDto> findAll() {
        return  modelMapper
                .map(roleRepository.findAll(),new TypeToken<List<RoleDto>>() {}.getType());
    }

    @Override
    @PreAuthorize("hasAuthority('authority.role.new')")
    public RoleDto saveRole(RoleDto roleDto) {
        if(roleDto == null){
            throw  new ResourceNotFoundException("authority.notfound");
        }
        Role role = modelMapper.map(roleDto,Role.class);
        role = roleRepository.save(role);

        return modelMapper.map(role,RoleDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('authority.role.edit')")
    public RoleDto updateRole(RoleDto roleDto) {
        if(roleDto == null || roleDto.getId() == null || roleDto.getId() == 0){
            throw  new ResourceNotFoundException("authority.notfound");
        }

        Role role = modelMapper.map(roleDto,Role.class);
        role = roleRepository.save(role);

        return modelMapper.map(role,RoleDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('authority.role.delete')")
    public boolean deleteRole(long id) {
        if(id == 0){
            throw  new ResourceNotFoundException("authority.notfound");
        }
        try{
            roleRepository.deleteById(id);
            return true;
        }catch (Exception ex){
            log.error("Role.Delete",ex);
            return false;
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('authority.role.new','authority.role.edit','authority.role.delete')")
    public DataTablesOutput<RoleDto> rolesPaging(@Valid DataTablesInput input) {
        Function<Role, RoleDto> toDto = entity -> toDto(entity);
        DataTablesOutput<RoleDto> output = roleRepository.findAll(input,toDto);
        return output;
    }

    @Override
    public RoleDto getByName(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if(!role.isPresent()){
            return null;
        }
        return modelMapper.map(role.get(),RoleDto.class);
    }

    @Override
    public RoleDto getById(long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent()){
            return null;
        }
        return modelMapper.map(role.get(),RoleDto.class);
    }

    @Override
    protected boolean exist(RoleDto dto) {
        return roleRepository.existsByName(dto.getName());
    }
}
