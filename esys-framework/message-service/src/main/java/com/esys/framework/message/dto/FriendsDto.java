package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.message.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Getter
@Setter
@NoArgsConstructor
public class FriendsDto extends AbstractDto {

    private String firstName;
    private String lastName;
    private String email;
    private UserStatus status;
    private String channel;
    private long unReadMessageCount;

    public FriendsDto(Long id, String email, String firstName, String lastName,
                      UserStatus status, long unReadMessageCount) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.unReadMessageCount = unReadMessageCount;
    }
}
