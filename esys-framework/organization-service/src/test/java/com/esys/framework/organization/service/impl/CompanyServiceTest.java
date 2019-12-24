package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.ICompanyService;
import com.esys.framework.organization.service.IGroupService;
import com.esys.framework.organization.service.IMainGroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainGroupService.class})
@DataJpaTest
@ContextConfiguration(classes = {CompanyServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyServiceTest {

    private CompanyDto dto;
    private CompanyDto dto2;

    @Autowired
    private IMainGroupService mainGroupService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private ICompanyService service;

    @Before
    public void setUp() {
        this.dto = getCompany();
        this.dto2 = getCompany2();
    }


    @Test
    public void save() throws Exception {
        ModelResult<MainGroupDto> mainGroupSaveResult = mainGroupService.save(getMainGroupDto());
        assertThat(mainGroupSaveResult.isSuccess()).isTrue();
        assertThat(mainGroupSaveResult.getData().getId()).isPositive();

        ModelResult<GroupDto> groupSaveResult =
                groupService.save(getGroupDto(mainGroupSaveResult.getData().getId()));
        assertThat(groupSaveResult.isSuccess()).isTrue();
        assertThat(groupSaveResult.getData().getId()).isPositive();

        dto.setIdGroup(groupSaveResult.getData().getId());
        dto2.setIdGroup(groupSaveResult.getData().getId());

        ModelResult<CompanyDto> companyDtoModelResult = service.save(dto);
        assertThat(companyDtoModelResult.isSuccess()).isTrue();
        assertThat(companyDtoModelResult.getData().getId()).isPositive();

        ModelResult<CompanyDto> companyDtoModelResult2 = service.save(dto2);
        assertThat(companyDtoModelResult2.isSuccess()).isTrue();
        assertThat(companyDtoModelResult2.getData().getId()).isPositive();

    }

    @Test
    public void update() throws Exception {

        saveCompany();
        ModelResult<List<CompanyDto>> list = service.getAll();

        ModelResult<CompanyDto> companyDtoModelResult1 =
                service.findOne(list.getData().get(0).getId());
        assertThat(companyDtoModelResult1.isSuccess()).isTrue();
        assertThat(companyDtoModelResult1.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        String updatedName= "Updated Company";
        CompanyDto updatedDto = companyDtoModelResult1.getData();
        updatedDto.setName(updatedName);

        ModelResult<CompanyDto> updatedResult =  service.update(updatedDto);
        assertThat(updatedResult.isSuccess()).isTrue();
        assertThat(updatedResult.getData().getName())
                .isEqualTo(updatedName);
    }

    @Test
    public void delete() throws Exception {
        saveCompany();
        ModelResult<List<CompanyDto>> list = service.getAll();

        assertThat(list.isSuccess()).isTrue();
        assertThat(list.getData().size()).isEqualTo(2);

        ModelResult response = service.delete(list.getData().get(0).getId());
        assertThat(response.isSuccess()).isTrue();

        response = service.delete(list.getData().get(1).getId());
        assertThat(response.isSuccess()).isTrue();

        list = service.getAll();
        assertThat(list.getData().size()).isEqualTo(0);
    }

    @Test
    public void getAll() throws Exception {
        saveCompany();
        ModelResult<List<CompanyDto>> companyDtoModelResult = service.getAll();
        assertThat(companyDtoModelResult.isSuccess()).isTrue();
        assertThat(companyDtoModelResult.getData().size()).isEqualTo(2);

        assertThat(companyDtoModelResult.getData().get(0).getName())
                .isEqualTo(dto.getName());
        assertThat(companyDtoModelResult.getData().get(1).getName())
                .isEqualTo(dto2.getName());

    }

    @Test
    public void findOne() throws Exception {

        final Group group = saveCompany();
        final ModelResult<CompanyDto> one = service.findOne(group.getCompanies().iterator().next().getId());
        assertThat(one.isSuccess()).isTrue();
        assertThat(one.getData().getName()).isEqualTo(group.getCompanies().iterator().next().getName());
    }

    @Test
    public void findCompaniesByGroupId() throws Exception {
        final Group group = saveCompany();
        final ModelResult<List<CompanyDto>> companiesByGroupId = service.findCompaniesByGroupId(group.getId());
        assertThat(companiesByGroupId.isSuccess()).isTrue();
        assertThat(companiesByGroupId.getData().size()).isEqualTo(2);

    }



    private Group saveCompany() throws Exception {

        mainGroupService.getAll().getData().stream().forEach(l -> {
            try {
                mainGroupService.delete(l.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ModelResult<MainGroupDto> mainGroupSaveResult = mainGroupService.save(getMainGroupDto());
        assertThat(mainGroupSaveResult.isSuccess()).isTrue();
        assertThat(mainGroupSaveResult.getData().getId()).isPositive();

        ModelResult<GroupDto> groupSaveResult =
                groupService.save(getGroupDto(mainGroupSaveResult.getData().getId()));
        assertThat(groupSaveResult.isSuccess()).isTrue();
        assertThat(groupSaveResult.getData().getId()).isPositive();

        dto.setIdGroup(groupSaveResult.getData().getId());
        dto2.setIdGroup(groupSaveResult.getData().getId());

        ModelResult<CompanyDto> companyDtoModelResult = service.save(dto);
        assertThat(companyDtoModelResult.isSuccess()).isTrue();
        assertThat(companyDtoModelResult.getData().getId()).isPositive();

        ModelResult<CompanyDto> companyDtoModelResult2 = service.save(dto2);
        assertThat(companyDtoModelResult2.isSuccess()).isTrue();
        assertThat(companyDtoModelResult2.getData().getId()).isPositive();


        Group group = new Group(groupSaveResult.getData().getId());
        group.setName(groupSaveResult.getData().getName());

        Set<Company> companies = new HashSet<>();

        Company company1 = new Company(companyDtoModelResult.getData().getId());
        company1.setName(companyDtoModelResult.getData().getName());

        Company company2 = new Company(companyDtoModelResult2.getData().getId());
        company2.setName(companyDtoModelResult2.getData().getName());

        companies.add(company1);
        companies.add(company2);
        group.setCompanies(companies);
        return group;
    }



    private MainGroupDto getMainGroupDto() {
        MainGroupDto dto = new MainGroupDto();
        dto.setName("Test Ana Grup");
        return dto;
    }



    private GroupDto getGroupDto(Long idMainGroup) {
        GroupDto dto = new GroupDto();
        dto.setName("Test Grup");
        dto.setIdMainGroup(idMainGroup);
        return dto;
    }

    private CompanyDto getCompany() {
        CompanyDto dto = new CompanyDto();
        dto.setName("Test Company");
        return dto;
    }

    private CompanyDto getCompany2() {
        CompanyDto dto = new CompanyDto();
        dto.setName("Test Company 2");
        return dto;
    }

    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"com.esys.framework"})
    static class Configuration{

        @Bean
        @Primary
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }

        @Bean
        public IMainGroupService mainGroupService() {
            return new MainGroupService();
        }

        @Bean
        public IGroupService groupService() {
            return new GroupService();
        }

        @Bean
        public ICompanyService companyService() {
            return new CompanyService();
        }

    }

}
