package com.esys.framework.message.repository;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.repository.SoftDeleteCrudRepository;
import com.esys.framework.message.dto.FriendsDto;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatUserStatus;
import com.esys.framework.message.enums.UserStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Transactional
@Repository
public interface ChatUserStatusRepository extends CrudRepository<ChatUserStatus, Long> {

    List<ChatUserStatus> findAllByUser(User user);

    List<ChatUserStatus> findByConnectionId(String connectionId);

    boolean existsByUserAndUserStatusNot(User user, UserStatus userStatus);

    boolean existsByUserAndUserStatus(User user, UserStatus userStatus);

    @Query("Select distinct new com.esys.framework.message.dto.FriendsDto(u.id,u.email,u.firstName,u.lastName, c.userStatus,0L) " +
            "FROM User u left join ChatUserStatus c on c.user = u WHERE u.id <> :excludedUser")
    List<FriendsDto> findFriendsListFor(@Param("excludedUser") long excludedUser);

}
