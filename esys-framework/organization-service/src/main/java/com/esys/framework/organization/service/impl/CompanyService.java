package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.CompanyRepository;
import com.esys.framework.organization.service.ICompanyService;
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
public class CompanyService implements ICompanyService {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param companyDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */

    @Override
    public ModelResult<CompanyDto> save(final CompanyDto companyDto) throws Exception {
        Company company = modelMapper.map(companyDto, Company.class);
        company = repository.save(company);
        ModelResult<CompanyDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(company, CompanyDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param companyDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<CompanyDto> update(CompanyDto companyDto) throws Exception {
        Company company = modelMapper.map(companyDto, Company.class);
        company = repository.save(company);
        return new  ModelResult.ModelResultBuilder<CompanyDto>()
                .setData(modelMapper.map(company,CompanyDto.class)).build();
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
    public ModelResult<List<CompanyDto>> getAll() {
        Page<Company> companies = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<CompanyDto> data=  companies.getContent().stream()
                .map(post -> modelMapper.map(post,CompanyDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<CompanyDto>>()
                .setData(data).build();
    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<CompanyDto> findOne(Long id) {
        Optional<Company> company = repository.findById(id);
        if(!company.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<CompanyDto>()
                .setData(modelMapper.map(company.get(),CompanyDto.class)).build();
    }


    @Override
    public ModelResult<List<CompanyDto>> findCompaniesByGroupId(long id) {
        List<Company> companies = repository.findCompaniesByGroup(new Group(id));
        List<CompanyDto> resultList = modelMapper.map(companies,new TypeToken<List<CompanyDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<CompanyDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<Company> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<Company> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<Company> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }


}
