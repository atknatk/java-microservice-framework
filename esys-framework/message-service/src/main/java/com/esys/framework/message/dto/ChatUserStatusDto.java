package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Getter
@Setter
public class ChatUserStatusDto extends AbstractDto {

    private BasicUserDto user;
    private UserStatus userStatus;
    private String connectionId;
}
