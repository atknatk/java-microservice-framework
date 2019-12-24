package com.esys.framework.core.dto.uaa;

import com.esys.framework.core.dto.base.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserGroupDto extends AbstractDto {

    @NotNull(message = "notnull.userGroup.name")
    @Size(min = 1,max = 60, message = "size.userGroup.name")
    private String name;

    @JsonFormat(pattern="dd.MM.yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime createdDate;

    private boolean isDefault;

    private boolean isPredefined;

    private boolean enabled;


    public UserGroupDto() {
    }

    public UserGroupDto(Long id) {
        super(id);
    }

    public UserGroupDto(@NotNull() @Size(min = 1, message = "size.userGroup.name}") String name) {
        this.name = name;
    }
}
