package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IMainGroupService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jdo.annotations.Order;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainGroupService.class})
@DataJpaTest
@ContextConfiguration(classes = {MainGroupServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MainGroupServiceTest {


    private MainGroupDto dto;
    private MainGroupDto dto2;

    @Autowired
    private IMainGroupService service;


    @Before
    public void setUp() {
        this.dto = getMainGroupDto();
        this.dto2 = getMainGroupDto2();
    }

    @Test
    @Transactional
    public void save() throws Exception {

        ModelResult<MainGroupDto> mainGroupDtoModelResult = service.save(dto);
        assertThat(mainGroupDtoModelResult.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult.getData().getId()).isPositive();

        ModelResult<MainGroupDto> mainGroupDtoModelResult2 = service.save(dto2);
        assertThat(mainGroupDtoModelResult2.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult2.getData().getId()).isPositive();
    }

    @Test
    public void update() throws Exception {
        save();
        ModelResult<List<MainGroupDto>> list = service.getAll();

        ModelResult<MainGroupDto> mainGroupDtoModelResult1 =
                service.findOne(list.getData().get(0).getId());
        assertThat(mainGroupDtoModelResult1.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult1.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        String updatedName= "Updated Main Group";
        MainGroupDto updatedDto = mainGroupDtoModelResult1.getData();
        updatedDto.setName(updatedName);

        ModelResult<MainGroupDto> updatedResult =  service.update(updatedDto);
        assertThat(updatedResult.isSuccess()).isTrue();
        assertThat(updatedResult.getData().getName())
                .isEqualTo(updatedName);
    }

    @Test
    public void delete() throws Exception {
        service.save(dto);
        ModelResult<List<MainGroupDto>> list = service.getAll();

        assertThat(list.isSuccess()).isTrue();

        ModelResult response = service.delete(list.getData().get(0).getId());
        assertThat(response.isSuccess()).isTrue();


    }

    @Test
    public void getAll() throws Exception {
        service.save(dto);
        ModelResult<List<MainGroupDto>> mainGroupDtoModelResult = service.getAll();
        assertThat(mainGroupDtoModelResult.isSuccess()).isTrue();
    }



    private MainGroupDto getMainGroupDto() {
        MainGroupDto dto = new MainGroupDto();
        dto.setName("Test Ana Grup");
        return dto;
    }

    private MainGroupDto getMainGroupDto2() {
        MainGroupDto dto = new MainGroupDto();
        dto.setName("Test Ana Grup2");
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

    }


}
