package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IBranchOfficeService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BranchOfficeService.class})
@DataJpaTest
@ContextConfiguration(classes = {BranchOfficeServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BranchOfficeServiceTest {

    private BranchOfficeDto dto;
    private BranchOfficeDto dto2;

    private CompanyDto companyDto;


    @Autowired
    private IMainGroupService mainGroupService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IBranchOfficeService service;


    @Before
    public void setUp() {
        this.dto = getBranchOffice();
        this.dto2 = getBranchOffice2();
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

        companyDto = companyDtoModelResult.getData();

        dto.setIdCompany(companyDto.getId());
        dto2.setIdCompany(companyDto.getId());

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult = service.save(dto);
        assertThat(branchOfficeDtoModelResult.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult.getData().getId()).isPositive();
        dto = branchOfficeDtoModelResult.getData();

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult2 = service.save(dto2);
        assertThat(branchOfficeDtoModelResult2.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult2.getData().getId()).isPositive();
        dto2 = branchOfficeDtoModelResult2.getData();
    }

    @Test
    public void update() throws Exception {
        saveFor();

        ModelResult<List<BranchOfficeDto>> list = service.getAll();

        ModelResult<BranchOfficeDto> modelResult1 =
                service.findOne(list.getData().get(0).getId());
        assertThat(modelResult1.isSuccess()).isTrue();
        assertThat(modelResult1.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        String updatedName= "Updated Branch Office";
        BranchOfficeDto updatedDto = modelResult1.getData();
        updatedDto.setName(updatedName);

        ModelResult<BranchOfficeDto> updatedResult =  service.update(updatedDto);
        assertThat(updatedResult.isSuccess()).isTrue();
        assertThat(updatedResult.getData().getName())
                .isEqualTo(updatedName);

    }

    @Test
    public void delete() throws Exception {

        saveFor();
        ModelResult<List<BranchOfficeDto>> list = service.getAll();

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
        ModelResult<List<BranchOfficeDto>> all = service.getAll();
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
        ModelResult<List<BranchOfficeDto>> list = service.getAll();

        ModelResult<BranchOfficeDto> modelResult =
                service.findOne(list.getData().get(0).getId());
        assertThat(modelResult.isSuccess()).isTrue();
        assertThat(modelResult.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        ModelResult<BranchOfficeDto> modelResult2 =
                service.findOne(list.getData().get(1).getId());
        assertThat(modelResult2.isSuccess()).isTrue();
        assertThat(modelResult2.getData().getName())
                .isEqualTo(list.getData().get(1).getName());


    }

    @Test
    public void findBranchOfficesByCompany() throws Exception {

        saveFor();
        final ModelResult<List<BranchOfficeDto>> branchOfficesByCompany = service.findBranchOfficesByCompany(companyDto.getId());
        assertThat(branchOfficesByCompany.isSuccess()).isTrue();
        assertThat(branchOfficesByCompany.getData().size()).isEqualTo(2);
    }



    public void  saveFor() throws Exception {

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
        companyDto = companyDtoModelResult.getData();

        dto.setIdCompany(companyDto.getId());
        dto2.setIdCompany(companyDto.getId());

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult = service.save(dto);
        assertThat(branchOfficeDtoModelResult.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult.getData().getId()).isPositive();
        dto = branchOfficeDtoModelResult.getData();

        ModelResult<BranchOfficeDto> branchOfficeDtoModelResult2 = service.save(dto2);
        assertThat(branchOfficeDtoModelResult2.isSuccess()).isTrue();
        assertThat(branchOfficeDtoModelResult2.getData().getId()).isPositive();
        dto2 = branchOfficeDtoModelResult2.getData();
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

    private BranchOfficeDto getBranchOffice() {
        BranchOfficeDto dto = new BranchOfficeDto();
        dto.setName("Test BranchOfficeDto");
        return dto;
    }

    private BranchOfficeDto getBranchOffice2() {
        BranchOfficeDto dto = new BranchOfficeDto();
        dto.setName("Test BranchOfficeDto 2");
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

    }
}
