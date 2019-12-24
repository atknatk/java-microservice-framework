package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.entity.ChatMessage;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
public class ChatChannelDto extends AbstractDto {

    private String channelName;
    private ArrayList<BasicUserDto> users;
    private ArrayList<ChatMessageDto> chatMessages;


}
