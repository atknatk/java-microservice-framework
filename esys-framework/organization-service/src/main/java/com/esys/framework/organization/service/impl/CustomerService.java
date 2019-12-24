package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.CustomerDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.entity.organization.Customer;
import com.esys.framework.core.entity.organization.Employee;
import com.esys.framework.organization.persistence.dao.CustomerRepository;
import com.esys.framework.organization.service.ICustomerService;
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
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param dto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<CustomerDto> save(CustomerDto dto) throws Exception {
        Customer customer = modelMapper.map(dto, Customer.class);
        customer = repository.save(customer);
        ModelResult<CustomerDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(customer, CustomerDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param customerDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<CustomerDto> update(CustomerDto customerDto) throws Exception {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customer = repository.save(customer);
        return new  ModelResult.ModelResultBuilder<CustomerDto>()
                .setData(modelMapper.map(customer,CustomerDto.class)).build();
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
    public ModelResult<List<CustomerDto>> getAll() {

        Page<Customer> customers = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<CustomerDto> data=  customers.getContent().stream()
                .map(post -> modelMapper.map(post,CustomerDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<CustomerDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<CustomerDto> findOne(Long id) {
        Optional<Customer> customer = repository.findById(id);
        if(!customer.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<CustomerDto>()
                .setData(modelMapper.map(customer.get(),CustomerDto.class)).build();
    }

    @Override
    public ModelResult<List<CustomerDto>> findCustomersByEmployee(long id) {
        List<Customer> customers = repository.findCustomersByEmployee(new Employee(id));
        List<CustomerDto> resultList = modelMapper.map(customers,new TypeToken<List<CustomerDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<CustomerDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<Customer> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<Customer> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<Customer> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }

}
