package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.message.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
public class NotificationDto<T> extends AbstractDto {

    private NotificationType type;

    private String contents;

    private long fromUserId;

    private T data;

}
