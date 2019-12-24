package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.DepartmentDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Department;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.DepartmentRepository;
import com.esys.framework.organization.service.IDepartmentService;
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
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param departmentDto
     * @return ModelResult ModelResult doner ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<DepartmentDto> save(DepartmentDto departmentDto) throws Exception {
        Department department = modelMapper.map(departmentDto, Department.class);
        department = repository.save(department);
        ModelResult<DepartmentDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(department, DepartmentDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param departmentDto
     * @return ModelResult ModelResult doner ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<DepartmentDto> update(DepartmentDto departmentDto) throws Exception {
        Department department = modelMapper.map(departmentDto, Department.class);
        department = repository.save(department);
        return new  ModelResult.ModelResultBuilder<DepartmentDto>()
                .setData(modelMapper.map(department,DepartmentDto.class)).build();
    }

    /**
     * Parametre olarak aldigi id' degerini siler.
     * @param id
     * @return ModelResult ModelResult doner ModelResult doner degeri doner
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
    public ModelResult<List<DepartmentDto>> getAll() {

        Page<Department> departments = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<DepartmentDto> data=  departments.getContent().stream()
                .map(post -> modelMapper.map(post,DepartmentDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<DepartmentDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<DepartmentDto> findOne(Long id) {
        Optional<Department> department = repository.findById(id);
        if(!department.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<DepartmentDto>()
                .setData(modelMapper.map(department.get(),DepartmentDto.class)).build();
    }

    @Override
    public ModelResult<List<DepartmentDto>> findDepartmentsByBranchOffice(long id) {
        List<Department> departments = repository.findDepartmentsByBranchOffice(new BranchOffice(id));
        List<DepartmentDto> resultList = modelMapper.map(departments,new TypeToken<List<DepartmentDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<DepartmentDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<Department> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<Department> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<Department> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }

}
