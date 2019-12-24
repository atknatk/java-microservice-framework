package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.*;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.*;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DepartmentService.class})
@DataJpaTest
@ContextConfiguration(classes = {DepartmentServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentServiceTest {


    private DepartmentDto dto;
    private DepartmentDto dto2;

    private BranchOfficeDto branchOfficeDto;

    @Autowired
    private IMainGroupService mainGroupService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IBranchOfficeService branchOfficeService;

    @Autowired
    private IDepartmentService service;

    @Before
    public void setUp() {
        this.dto = getDepartmentDto();
        this.dto2 = getDepartmentDto2();
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

        ModelResult<CompanyDto> companyDtoModelResult =
                companyService.save(getCompany(groupSaveResult.getData().getId()));
        assertThat(companyDtoModelResult.isSuccess()).isTrue();
        assertThat(companyDtoModelResult.getData().getId()).isPositive();

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult =
                branchOfficeService.save(getBranchOffice(companyDtoModelResult.getData().getId()));
        assertThat(branchOfficeDtoModelResult.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult.getData().getId()).isPositive();


        branchOfficeDto = branchOfficeDtoModelResult.getData();

        dto.setIdBranchOffice(branchOfficeDto.getId());
        dto2.setIdBranchOffice(branchOfficeDto.getId());

        final ModelResult<DepartmentDto> save = service.save(dto);
        assertThat(save.isSuccess()).isTrue();
        assertThat(save.getData().getId()).isPositive();
        dto = save.getData();

        final ModelResult<DepartmentDto> save1 = service.save(dto2);
        assertThat(save1.isSuccess()).isTrue();
        assertThat(save1.getData().getId()).isPositive();
        dto2 = save1.getData();

    }

    @Test
    public void update() throws Exception {
        saveFor();

        final ModelResult<List<DepartmentDto>> list = service.getAll();

        ModelResult<DepartmentDto> modelResult1 =
                service.findOne(list.getData().get(0).getId());
        assertThat(modelResult1.isSuccess()).isTrue();
        assertThat(modelResult1.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        String updatedName= "Updated";
        DepartmentDto updatedDto = modelResult1.getData();
        updatedDto.setName(updatedName);

        ModelResult<DepartmentDto> updatedResult =  service.update(updatedDto);
        assertThat(updatedResult.isSuccess()).isTrue();
        assertThat(updatedResult.getData().getName())
                .isEqualTo(updatedName);
    }

    @Test
    public void delete() throws Exception {
        saveFor();
        ModelResult<List<DepartmentDto>> list = service.getAll();

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
        saveFor();
        ModelResult<List<DepartmentDto>> all = service.getAll();
        assertThat(all.isSuccess()).isTrue();
        assertThat(all.getData().size()).isEqualTo(2);

        assertThat(all.getData().get(0).getName())
                .isEqualTo(dto.getName());
        assertThat(all.getData().get(1).getName())
                .isEqualTo(dto2.getName());
    }

    @Test
    public void findOne() throws Exception {
        saveFor();
        ModelResult<List<DepartmentDto>> list = service.getAll();

        ModelResult<DepartmentDto> modelResult =
                service.findOne(list.getData().get(0).getId());
        assertThat(modelResult.isSuccess()).isTrue();
        assertThat(modelResult.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        ModelResult<DepartmentDto> modelResult2 =
                service.findOne(list.getData().get(1).getId());
        assertThat(modelResult2.isSuccess()).isTrue();
        assertThat(modelResult2.getData().getName())
                .isEqualTo(list.getData().get(1).getName());

    }

    @Test
    public void findDepartmentsByBranchOffice() throws Exception {
        saveFor();
        final ModelResult<List<DepartmentDto>> result= service.findDepartmentsByBranchOffice(branchOfficeDto.getId());
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData().size()).isEqualTo(2);
    }


    public void saveFor() throws Exception {
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

        ModelResult<CompanyDto> companyDtoModelResult =
                companyService.save(getCompany(groupSaveResult.getData().getId()));
        assertThat(companyDtoModelResult.isSuccess()).isTrue();
        assertThat(companyDtoModelResult.getData().getId()).isPositive();

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult =
                branchOfficeService.save(getBranchOffice(companyDtoModelResult.getData().getId()));
        assertThat(branchOfficeDtoModelResult.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult.getData().getId()).isPositive();


        branchOfficeDto = branchOfficeDtoModelResult.getData();

        dto.setIdBranchOffice(branchOfficeDto.getId());
        dto2.setIdBranchOffice(branchOfficeDto.getId());

        final ModelResult<DepartmentDto> save = service.save(dto);
        assertThat(save.isSuccess()).isTrue();
        assertThat(save.getData().getId()).isPositive();
        dto = save.getData();

        final ModelResult<DepartmentDto> save1 = service.save(dto2);
        assertThat(save1.isSuccess()).isTrue();
        assertThat(save1.getData().getId()).isPositive();
        dto2 = save1.getData();

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


    private CompanyDto getCompany(Long idGroup) {
        CompanyDto dto = new CompanyDto();
        dto.setName("Test Company");
        dto.setIdGroup(idGroup);
        return dto;
    }

    private BranchOfficeDto getBranchOffice(Long companyId) {
        BranchOfficeDto dto = new BranchOfficeDto();
        dto.setName("Test BranchOfficeDto");
        dto.setIdCompany(companyId);
        return dto;
    }

    private DepartmentDto getDepartmentDto() {
        DepartmentDto dto = new DepartmentDto();
        dto.setName("Test DepartmentDto");
        return dto;
    }

    private DepartmentDto getDepartmentDto2() {
        DepartmentDto dto = new DepartmentDto();
        dto.setName("Test DepartmentDto 2");
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

        @Bean
        public IBranchOfficeService branchOfficeService() {
            return new BranchOfficeService();
        }

        @Bean
        public IDepartmentService departmentService() {
            return new DepartmentService();
        }

    }

}
