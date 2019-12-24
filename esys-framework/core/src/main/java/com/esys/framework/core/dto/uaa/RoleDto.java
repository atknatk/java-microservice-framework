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


public class RoleDto extends AbstractDto {

    @Setter
    @NotNull()
    @Size(min = 1, message = "size.roleDto.name")
    private String name;

    @Getter
    @Setter
//    @JsonFormat(pattern="dd.MM.yyyy")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDate;

    @Getter
    @Setter
    private boolean isDefault;

    @Getter
    @Setter
    private boolean isPredefined;

    @Getter
    @Setter
    private boolean isBpm;

    @Getter
    @Setter
    private List<AuthorityDto> authorities;

    public String getName() {
        return name;
    }

    public RoleDto() {
    }

    public RoleDto(Long id) {
        super(id);
    }

    public RoleDto(@NotNull() @Size(min = 1, message = "size.roleDto.name") String name) {
        this.name = name;
    }
}
