package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.Employee;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.GroupRepository;
import com.esys.framework.organization.service.IGroupService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param groupDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    public ModelResult<GroupDto> save(final GroupDto groupDto) throws Exception {
        Group group = modelMapper.map(groupDto, Group.class);
        group = repository.save(group);
        ModelResult<GroupDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(group, GroupDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param groupDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<GroupDto> update(GroupDto groupDto)  {
        Group group = modelMapper.map(groupDto, Group.class);
        group = repository.save(group);
        return new  ModelResult.ModelResultBuilder<GroupDto>()
                .setData(modelMapper.map(group,GroupDto.class)).build();
    }

    /**
     * Parametre olarak aldigi id' degerini siler.
     * @param id
     * @return ModelResult ModelResult doner degeri doner
     * @throws Exception
     */
    @Override
    public ModelResult<Boolean> delete(Long id) throws Exception {
        if(id <= 0){
            throw new Exception();
        }
        repository.deleteById(id);
        return new  ModelResult.ModelResultBuilder().setStatus(0).build();
    }

    /**
     * Yuklenen son 1000 kaydini doner.
     * @return
     */
    @Override
    public ModelResult<List<GroupDto>> getAll() {
        Page<Group> groups = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<GroupDto> data=  groups.getContent().stream()
                .map(post -> modelMapper.map(post,GroupDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<GroupDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<GroupDto> findOne(Long id) {
        Optional<Group> group = repository.findById(id);
        if(!group.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<GroupDto>()
                .setData(modelMapper.map(group.get(),GroupDto.class)).build();
    }

    @Override
    public ModelResult<List<GroupDto>> findGroupsByMainGroup(long id) {
        List<Group> group = repository.findGroupsByMainGroup(new MainGroup(id));
        List<GroupDto> resultList = modelMapper.map(group,new TypeToken<List<GroupDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<GroupDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<Group> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }


    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<Group> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<Group> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }
}
