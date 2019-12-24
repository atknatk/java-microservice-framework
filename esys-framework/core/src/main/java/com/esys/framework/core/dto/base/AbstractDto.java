package com.esys.framework.core.dto.base;

import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.model.ModelResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;


@ToString
@Getter
@Setter
@Slf4j
public class AbstractDto implements IDto{

    private Long id;

    public AbstractDto() {
    }

    public AbstractDto(Long id) {
        this.id = id;
    }

    public ModelResult isIdEmpty(MessageSource source){
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(source);
        if(id == null){
            log.warn("Dto id is null {}", this);
            builders.setMessageKey("core.dto.id.null");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        if(id.longValue() == 0){
            log.warn("Dto id is zero {}", this);
            builders.setMessageKey("core.dto.id.zero");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        return builders.setStatus(ResultStatusCode.SUCCESS).build();
    }
}
