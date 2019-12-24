package com.esys.framework.core.dto.organization;

import com.esys.framework.core.dto.base.AbstractDto;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MainGroupDto extends AbstractDto {

    @NotNull
    private String name;
}
