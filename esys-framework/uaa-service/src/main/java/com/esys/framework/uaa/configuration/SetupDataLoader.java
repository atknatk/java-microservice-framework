package com.esys.framework.uaa.configuration;

import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import com.esys.framework.core.repository.IUserRepository;
import com.esys.framework.uaa.repository.IAuthorityRepository;
import com.esys.framework.uaa.repository.IRoleRepository;
import com.esys.framework.uaa.service.IUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Profile("development")
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private IAuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }



        // == create initial privileges
        Authority a1 = createPrivilegeIfNotFound("authority.authority.edit");
        Authority a2 = createPrivilegeIfNotFound("authority.authority.delete");
        Authority a3 = createPrivilegeIfNotFound("authority.authority.new");

        Authority a4 = createPrivilegeIfNotFound("authority.user.edit");
        Authority a5 = createPrivilegeIfNotFound("authority.user.delete");
        Authority a6 = createPrivilegeIfNotFound("authority.user.new");

        Authority a7 = createPrivilegeIfNotFound("authority.usergroup.edit");
        Authority a8 = createPrivilegeIfNotFound("authority.usergroup.delete");
        Authority a9 = createPrivilegeIfNotFound("authority.usergroup.new");

        Authority a10 = createPrivilegeIfNotFound("authority.log.view");
        Authority a11 = createPrivilegeIfNotFound("authority.log.ownview");

        Authority a12 = createPrivilegeIfNotFound("authority.organization.edit");
        Authority a13 = createPrivilegeIfNotFound("authority.organization.delete");
        Authority a14 = createPrivilegeIfNotFound("authority.organization.new");

        Authority a15 = createPrivilegeIfNotFound("authority.product.upload");
        Authority a16 = createPrivilegeIfNotFound("authority.product.version");
        Authority a17 = createPrivilegeIfNotFound("authority.product.export");

        Authority a18 = createPrivilegeIfNotFound("authority.dashboard.view");
        Authority a19 = createPrivilegeIfNotFound("authority.show.password");

        Authority a20 = createPrivilegeIfNotFound("authority.role.edit");
        Authority a21 = createPrivilegeIfNotFound("authority.role.delete");
        Authority a22 = createPrivilegeIfNotFound("authority.role.new");

        Authority a23 = createPrivilegeIfNotFound("authority.price.dollar");


        // == create initial roles
        final List<Authority> adminPrivileges = new ArrayList<Authority>(Arrays.asList(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23));
        final List<Authority> userPrivileges = new ArrayList<Authority>(Arrays.asList(a18));
        final Role adminRole = createRoleIfNotFound("Admin", adminPrivileges,false);
        createRoleIfNotFound("User", userPrivileges,true);

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "test", new ArrayList<>(Arrays.asList(adminRole)));
        createUserIfNotFound("atakan.atik@everva.com.tr", "Atakan", "Atik", "test", new ArrayList<>(Arrays.asList(adminRole)));
        createUserIfNotFound("gokhan.karahan@isisbilisim.com.tr", "Gökhan", "Karahan", "test", new ArrayList<>(Arrays.asList(adminRole)));
        createUserIfNotFound("mustafa.yilmaz@isisbilisim.com.tr", "Mustafa", "Yılmaz", "test", new ArrayList<>(Arrays.asList(adminRole)));

        alreadySetup = true;
    }

    private final Authority createPrivilegeIfNotFound(final String name) {
        Authority privilege = authorityRepository.findByName(name);
        if (privilege == null) {
            privilege = new Authority(name);
            privilege = authorityRepository.save(privilege);
        }
        return privilege;
    }

    private final Role createRoleIfNotFound(final String name, final Collection<Authority> privileges,
    final boolean isDefault) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        Role role = new Role(name);
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
        }
        role.setAuthorities(privileges);
        role.setPredefined(true);
        role.setDefault(isDefault);
        role = roleRepository.save(role);
        return role;
    }

    private final User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Collection<Role> roles) {
        User user = userRepository.getUserByEmailAndDeletedIsFalse(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);

        UserGroup userGroup  = userGroupService.saveUserGroupIfNotExist(new UserGroup(email.split("@")[1]));
        if(userGroup.getUsers() == null) userGroup.setUsers(new ArrayList<>());
        userGroup.getUsers().add(user);
        userGroupService.saveUserGroupIfNotExist(userGroup);
        return user;
    }

}
