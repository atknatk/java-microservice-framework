package com.esys.framework.core.dto.uaa;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.uaa.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Getter
@Setter
public class BasicUserDto extends AbstractDto {

    @JsonProperty("user_name")
    private String email;

    @NotNull
    @Size(min = 1, message = "uaa.validation.size.userDto.firstName")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "uaa.validation.size.userDto.lastName")
    private String lastName;

    private String domain = "esys";

//    private AuthorityDto authorities;


    public BasicUserDto(){

    }

    public BasicUserDto(Long id, String email, @NotNull @Size(min = 1, message = "uaa.validation.size.userDto.firstName") String firstName, @NotNull @Size(min = 1, message = "uaa.validation.size.userDto.lastName") String lastName, String domain) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.domain = domain;
    }

    public BasicUserDto(UserDto userDto){
        super(userDto.getId());
        this.email = userDto.getEmail();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.domain = userDto.getDomain();
    }

    public BasicUserDto(User userDto){
        super(userDto.getId());
        this.email = userDto.getEmail();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.domain = userDto.getDomain();
    }

}
