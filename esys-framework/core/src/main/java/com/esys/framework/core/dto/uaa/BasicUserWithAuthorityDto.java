package com.esys.framework.core.dto.uaa;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Getter
@Setter
public class BasicUserWithAuthorityDto extends AbstractDto {

    @JsonProperty("user_name")
    private String email;

    private String firstName;

    private String lastName;

    private String domain = "esys";

    private List<String> authorities;

    public BasicUserWithAuthorityDto(){

    }


}
