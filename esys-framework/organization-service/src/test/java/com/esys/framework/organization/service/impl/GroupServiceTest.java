package com.esys.framework.organization.service.impl;

import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IGroupService;
import com.esys.framework.organization.service.IMainGroupService;
import com.google.common.collect.Lists;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainGroupService.class})
@DataJpaTest
@ContextConfiguration(classes = {GroupServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupServiceTest {


    private GroupDto dto;
    private GroupDto dto2;

    @Autowired
    private IMainGroupService mainGroupService;

    @Autowired
    private IGroupService service;

    @Before
    public void setUp() {
        this.dto = getGroupDto();
        this.dto2 = getGroupDto2();
    }

    @Test
    public void save() throws Exception {

        ModelResult<MainGroupDto> mainGroupSaveResult = mainGroupService.save(getMainGroupDto());
        assertThat(mainGroupSaveResult.isSuccess()).isTrue();
        assertThat(mainGroupSaveResult.getData().getId()).isPositive();


        ModelResult<List<MainGroupDto>> mainGroupDtoModelResult  =mainGroupService.getAll();
        assertThat(mainGroupDtoModelResult.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult.getData().size()).isEqualTo(1);
        dto.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());
        dto2.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());

        ModelResult<GroupDto> groupDtoModelResult = service.save(dto);
        assertThat(groupDtoModelResult.isSuccess()).isTrue();
        assertThat(groupDtoModelResult.getData().getId()).isPositive();

        ModelResult<GroupDto> groupDtoModelResult2 = service.save(dto2);
        assertThat(groupDtoModelResult2.isSuccess()).isTrue();
        assertThat(groupDtoModelResult2.getData().getId()).isPositive();
    }

    @Test
    public void update() throws Exception {
        saveGroup();
        ModelResult<List<GroupDto>> list = service.getAll();

        ModelResult<GroupDto> groupDtoModelResult1 =
                service.findOne(list.getData().get(0).getId());
        assertThat(groupDtoModelResult1.isSuccess()).isTrue();
        assertThat(groupDtoModelResult1.getData().getName())
                .isEqualTo(list.getData().get(0).getName());

        String updatedName= "Updated Group";
        GroupDto updatedDto = groupDtoModelResult1.getData();
        updatedDto.setName(updatedName);

        ModelResult<GroupDto> updatedResult =  service.update(updatedDto);
        assertThat(updatedResult.isSuccess()).isTrue();
        assertThat(updatedResult.getData().getName())
                .isEqualTo(updatedName);

    }

    @Test
    public void delete() throws Exception {
        saveGroup();
        ModelResult<List<GroupDto>> list = service.getAll();

        assertThat(list.isSuccess()).isTrue();
        assertThat(list.getData().size()).isEqualTo(1);

        ModelResult response = service.delete(list.getData().get(0).getId());
        assertThat(response.isSuccess()).isTrue();

        list = service.getAll();
        assertThat(list.getData().size()).isEqualTo(0);
    }

    @Test
    public void getAll() throws Exception {

        saveGroup();
        ModelResult<List<GroupDto>> groupDtoModelResult = service.getAll();
        assertThat(groupDtoModelResult.isSuccess()).isTrue();
        assertThat(groupDtoModelResult.getData().size()).isEqualTo(1);

        assertThat(groupDtoModelResult.getData().get(0).getName())
                .isEqualTo(dto.getName());
    }

    @Test
    public void findOne() throws Exception {

        GroupDto groupDto = saveGroup();
        final ModelResult<GroupDto> one = service.findOne(groupDto.getId());
        assertThat(one.isSuccess()).isTrue();
        assertThat(one.getData().getName()).isEqualTo(groupDto.getName());
    }

    @Test
    public void findGroupsByMainGroup() throws Exception {

        final MainGroup groupDto = saveGroupRetMain();
        final ModelResult<List<GroupDto>> groupsByMainGroup = service.findGroupsByMainGroup(groupDto.getId());
        assertThat(groupsByMainGroup.isSuccess()).isTrue();
        assertThat(groupsByMainGroup.getData().size()).isEqualTo(1);
        assertThat(groupsByMainGroup.getData().get(0).getName()).isEqualTo(groupDto.getGroups().iterator().next().getName());


    }

    private GroupDto saveGroup() throws Exception {
        service.getAll().getData().stream().forEach(l -> {
            try {
                service.delete(l.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ModelResult<MainGroupDto> mainGroupSaveResult = mainGroupService.save(getMainGroupDto());
        assertThat(mainGroupSaveResult.isSuccess()).isTrue();
        assertThat(mainGroupSaveResult.getData().getId()).isPositive();


        ModelResult<List<MainGroupDto>> mainGroupDtoModelResult  =mainGroupService.getAll();
        assertThat(mainGroupDtoModelResult.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult.getData().size()).isEqualTo(1);
        dto.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());
        dto2.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());

        ModelResult<GroupDto> groupDtoModelResult = service.save(dto);
        assertThat(groupDtoModelResult.isSuccess()).isTrue();
        assertThat(groupDtoModelResult.getData().getId()).isPositive();

        return groupDtoModelResult.getData();

    }

    private MainGroup saveGroupRetMain() throws Exception {
        service.getAll().getData().stream().forEach(l -> {
            try {
                service.delete(l.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ModelResult<MainGroupDto> mainGroupSaveResult = mainGroupService.save(getMainGroupDto());
        assertThat(mainGroupSaveResult.isSuccess()).isTrue();
        assertThat(mainGroupSaveResult.getData().getId()).isPositive();


        ModelResult<List<MainGroupDto>> mainGroupDtoModelResult  =mainGroupService.getAll();
        assertThat(mainGroupDtoModelResult.isSuccess()).isTrue();
        assertThat(mainGroupDtoModelResult.getData().size()).isEqualTo(1);
        dto.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());
        dto2.setIdMainGroup(mainGroupDtoModelResult.getData().get(0).getId());

        ModelResult<GroupDto> groupDtoModelResult = service.save(dto);
        assertThat(groupDtoModelResult.isSuccess()).isTrue();
        assertThat(groupDtoModelResult.getData().getId()).isPositive();

        MainGroup mainGroup = new MainGroup();

        mainGroup.setId(mainGroupSaveResult.getData().getId());
        Set<Group> groups = new HashSet<>();
        final Group group = new Group(groupDtoModelResult.getData().getId());
        group.setName(groupDtoModelResult.getData().getName());
        groups.add(group);
        mainGroup.setGroups(groups);
        return mainGroup;

    }


    private MainGroupDto getMainGroupDto() {
        MainGroupDto dto = new MainGroupDto();
        dto.setName("Test Ana Grup");
        return dto;
    }

    private GroupDto getGroupDto() {
        GroupDto dto = new GroupDto();
        dto.setName("Test Grup");
        return dto;
    }

    private GroupDto getGroupDto2() {
        GroupDto dto = new GroupDto();
        dto.setName("Test Grup 2");
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

    }
}
