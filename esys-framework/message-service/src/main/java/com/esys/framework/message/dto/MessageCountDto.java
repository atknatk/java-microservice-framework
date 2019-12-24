package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.uaa.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
public class MessageCountDto extends AbstractDto {
    private Long count;
    private String channelName;

    public MessageCountDto(Long id, Long count, String channelName) {
        super(id);
        this.count = count;
        this.channelName = channelName;
    }
}
