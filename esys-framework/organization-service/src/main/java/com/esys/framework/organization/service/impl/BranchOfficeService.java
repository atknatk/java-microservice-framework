package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.persistence.dao.BranchOfficeRepository;
import com.esys.framework.organization.service.IBranchOfficeService;
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
public class BranchOfficeService implements IBranchOfficeService {

    @Autowired
    private BranchOfficeRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Parametre olarak aldığı dto'yu kaydeder. ModelResult modeli içerisinde kaydettiği objenin veritabanı kaydını döner.
     * @param branchOfficeDto branchOfficeDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<BranchOfficeDto> save(BranchOfficeDto branchOfficeDto) throws Exception {
        BranchOffice branchOffice = modelMapper.map(branchOfficeDto, BranchOffice.class);
        branchOffice = repository.save(branchOffice);
        ModelResult<BranchOfficeDto> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(branchOffice, BranchOfficeDto.class)).build();
        return result;
    }

    /**
     * Parametre olarak aldığı dto'yu gunceller. ModelResult modeli içerisinde guncellenen objenin veritabanı kaydını döner.
     * @param branchOfficeDto
     * @return ModelResult ModelResult doner
     * @throws Exception
     */
    @Override
    public ModelResult<BranchOfficeDto> update(BranchOfficeDto branchOfficeDto) throws Exception {
        BranchOffice branchOffice = modelMapper.map(branchOfficeDto, BranchOffice.class);
        branchOffice = repository.save(branchOffice);
        return new  ModelResult.ModelResultBuilder<BranchOfficeDto>()
                .setData(modelMapper.map(branchOffice,BranchOfficeDto.class)).build();
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
    public ModelResult<List<BranchOfficeDto>> getAll() {

        Page<BranchOffice> branchOffices = repository.findAll(
                new PageRequest(0,1000,new Sort(Sort.Direction.DESC, "id")));
        List<BranchOfficeDto> data=  branchOffices.getContent().stream()
                .map(post -> modelMapper.map(post,BranchOfficeDto.class))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<BranchOfficeDto>>()
                .setData(data).build();

    }

    /**
     * Verilen id'deki degeri doner eger yoksa null doner.
     * @param id
     * @return
     */
    @Override
    public ModelResult<BranchOfficeDto> findOne(Long id) {
        Optional<BranchOffice> branchOffice = repository.findById(id);
        if(!branchOffice.isPresent()){
            return null;
        }
        return new  ModelResult.ModelResultBuilder<BranchOfficeDto>()
                .setData(modelMapper.map(branchOffice.get(),BranchOfficeDto.class)).build();
    }

    @Override
    public ModelResult<List<BranchOfficeDto>> findBranchOfficesByCompany(long id) {
        List<BranchOffice> branchOffices = repository.findBranchOfficesByCompany(new Company(id));
        List<BranchOfficeDto> resultList = modelMapper.map(branchOffices,new TypeToken<List<BranchOfficeDto>>() {}.getType());
        return new  ModelResult.ModelResultBuilder<List<BranchOfficeDto>>()
                .setData(resultList).build();
    }

    /**
     * Veriye bagli user listesi donus yapar.
     * @param id
     * @return
     */
    @Override
    public List<BasicUserDto> users(Long id) {
        final Optional<BranchOffice> byId = repository.findById(id);
        if(!byId.isPresent()) return new ArrayList<>();
        Hibernate.initialize(byId.get().getUsers());
        return modelMapper.map(byId.get().getUsers(),new TypeToken<List<BasicUserDto>>() {}.getType());
    }

    @Override
    public void saveUsers(Long id, List<User> users) {
        final Optional<BranchOffice> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        byId.get().setUsers(Stream.concat(byId.get().getUsers().stream(),users.stream())
                .collect(Collectors.toList()));
        repository.save(byId.get());

    }

    @Override
    public void delete(Long id, User user) {
        final Optional<BranchOffice> byId = repository.findById(id);
        if(!byId.isPresent()) return;
        final List<User> collect = byId.get().getUsers().stream().filter(l -> l.getId().equals(user.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()) byId.get().getUsers().remove(collect.get(0));
        repository.save(byId.get());
    }
}
