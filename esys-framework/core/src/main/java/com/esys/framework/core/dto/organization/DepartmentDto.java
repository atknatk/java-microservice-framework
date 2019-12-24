package com.esys.framework.core.dto.organization;

import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Employee;
import com.esys.framework.core.entity.uaa.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class DepartmentDto extends AuthorityDto {

    @NotNull
    private String name;

    @NotNull
    private Long idBranchOffice;
}
