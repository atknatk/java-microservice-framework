package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.MainGroupRepository;
import com.esys.framework.organization.service.IMainGroupService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class MainGroupService implements IMainGroupService {

    @Autowired
    private MainGroupRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param mainGroupDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    public ModelResult<MainGroupDto> save(final MainGroupDto mainGroupDto) throws Exception {
        MainGroup mainGroup = modelMapper.map(mainGroupDto, MainGroup.class);
        mainGroup = repository.save(mainGroup);
        ModelResult<MainGroupDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(mainGroup,MainGroupDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param mainGroupDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    public ModelResult<MainGroupDto> update(final MainGroupDto mainGroupDto) throws Exception {
        MainGroup mainGroup = modelMapper.map(mainGroupDto, MainGroup.class);
        mainGroup = repository.save(mainGroup);
         return new  ModelResult.ModelResultBuilder<MainGroupDto>()
                .setData(modelMapper.map(mainGroup,MainGroupDto.class)).build();
    }

    /**
     * Parametre olarak aldigi id' degerini siler.
     * @param id
     * @return ModelResult ModelResult doner degeri doner
     * @throws Exception
     */
    public ModelResult delete(final Long id) throws Exception {
        if(id <= 0){
            throw new Exception();
        }
        repository.deleteById(id);
        return new  ModelResult.ModelResultBuilder().setStatus(0).build();
    }


    /**
     * Yuklenen son 1000 kaydini doner.
     * @return ModelResult
     */
    public ModelResult<List<MainGroupDto>> getAll() {
        Iterable<MainGroup> mainGroups = repository.findAll();
        List<MainGroupDto> data= Lists.newArrayList(mainGroups).stream()
                .map(post -> modelMapper.map(post,MainGroupDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<MainGroupDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return ModelResult
     */
    public ModelResult<MainGroupDto> findOne(final Long id) throws Exception {
        Optional<MainGroup> mainGroup = repository.findById(id);
        if(!mainGroup.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<MainGroupDto>()
                .setData(modelMapper.map(mainGroup.get(),MainGroupDto.class)).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return List
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<MainGroup> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<MainGroup> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<MainGroup> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }

}
