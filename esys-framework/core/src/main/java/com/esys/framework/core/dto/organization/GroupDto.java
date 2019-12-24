package com.esys.framework.core.dto.organization;

import com.esys.framework.core.consts.FieldSecurityConstants;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.policy.AuthorityBasedFieldSecurityPolicy;
import com.esys.framework.core.security.field.policy.EvalulationLogic;
import com.esys.framework.core.security.field.policy.RoleBasedFieldSecurityPolicy;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto extends AbstractDto {

    @NotNull
    private String name;

    @NotNull
    //@SecureField( policyBeans = {FieldSecurityConstants.CUSTOM_FIELD_POLICY},overrideMessage = "Yasak")
    //@SecureField(policyClasses = RoleBasedFieldSecurityPolicy.class,roles= {"ROLE_ADMIN","ROLE_DASHBORD"},roleLogic = EvalulationLogic.OR)
    //@SecureField(policyClasses = AuthorityBasedFieldSecurityPolicy.class,authorities = {"Field"})
    private Long idMainGroup;
}
