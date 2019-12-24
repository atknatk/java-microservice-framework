package com.esys.framework.uaa.repository;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import com.esys.framework.core.repository.SoftDeleteCrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IUserGroupRepository extends SoftDeleteCrudRepository<UserGroup,Long> {

    Optional<UserGroup> getUserGroupByName(String name);

    List<UserGroup> getUserGroupByUsers(Collection<User> users);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM UserGroup u WHERE u.name = ?1")
    boolean existsByName(String name);

    boolean existsById(long id);

    @Query("select ug from UserGroup ug JOIN FETCH ug.users where ug.id =?1")
    Optional<UserGroup> findByIdWithUser(long id);
}
