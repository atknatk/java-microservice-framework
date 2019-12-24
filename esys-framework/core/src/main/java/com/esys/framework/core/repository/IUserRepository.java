package com.esys.framework.core.repository;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.repository.SoftDeleteCrudRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends SoftDeleteCrudRepository<User,Long> {


    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    String USERS_BY_EMAIL_AND_DOMAIN_CACHE = "usersByEmailAndDomain";


    @EntityGraph(attributePaths = "roles")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    User getUserByEmailAndDeletedIsFalse(String email);

    @EntityGraph(attributePaths = "roles")
   // @Cacheable(cacheNames = USERS_BY_EMAIL_AND_DOMAIN_CACHE)
    @Query("select u from User u JOIN FETCH u.roles  where u.email = :email and u.domain = :domain")
    User getUser(String email,String domain);

    @Query("select u from User u JOIN FETCH u.roles  where u.id = ?1")
    Optional<User> findByIdWithRoles(Long id);

    Optional<User> getUserById(Long id);

    @Query("update User u set u.lastLoginDate=CURRENT_DATE, u.loginAttemptTryCount = 0 where u.id = ?1")
    @Transactional
    @Modifying
    void updateLastLoginTime(Long id);

    @Query("update User u set u.loginAttemptTryCount=(u.loginAttemptTryCount+1) where u.id = ?1")
    @Transactional
    @Modifying
    void updateLoginAttemptTryCount(Long id);

    @Query("update User u set u.enabled=?2 where u.id = ?1")
    @Transactional
    @Modifying
    void updateEnable(Long id,boolean enable);

    @Transactional
    @Query("select u.password from User u where u.email = ?1")
    String getPassword(String mail);

    @Transactional
    @Query("select new com.esys.framework.core.dto.uaa.BasicUserDto(u.id,u.email,u.firstName,u.lastName,u.domain) from User u ")
    List<BasicUserDto> findAllBasic();

}
