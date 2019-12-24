package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Customer;
import com.esys.framework.core.entity.organization.Department;
import com.esys.framework.core.entity.organization.Employee;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.EmployeeRepository;
import com.esys.framework.organization.service.IEmployeeService;
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
public class EmployeeService implements IEmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param employeeDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<EmployeeDto> save(EmployeeDto employeeDto) throws Exception {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee = repository.save(employee);
        ModelResult<EmployeeDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(employee, EmployeeDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param employeeDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<EmployeeDto> update(EmployeeDto employeeDto) throws Exception {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee = repository.save(employee);
        return new  ModelResult.ModelResultBuilder<EmployeeDto>()
                .setData(modelMapper.map(employee,EmployeeDto.class)).build();
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
    public ModelResult<List<EmployeeDto>> getAll() {

        Page<Employee> employees = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<EmployeeDto> data=  employees.getContent().stream()
                .map(post -> modelMapper.map(post,EmployeeDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<EmployeeDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<EmployeeDto> findOne(Long id)  {
        Optional<Employee> employee = repository.findById(id);
        if(!employee.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<EmployeeDto>()
                .setData(modelMapper.map(employee.get(),EmployeeDto.class)).build();
    }

    @Override
    public ModelResult<List<EmployeeDto>> findEmployeesByDepartment(long id) {
        List<Employee> employees = repository.findEmployeesByDepartment(new Department(id));
        List<EmployeeDto> resultList = modelMapper.map(employees,new TypeToken<List<EmployeeDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<EmployeeDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<Employee> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<Employee> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<Employee> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }

}
