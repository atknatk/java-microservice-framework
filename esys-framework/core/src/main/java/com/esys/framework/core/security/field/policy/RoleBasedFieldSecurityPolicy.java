package com.esys.framework.core.security.field.policy;

import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.principal.PrincipalProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RoleBasedFieldSecurityPolicy implements FieldSecurityPolicy {

    private final PrincipalProvider principalProvider;

    public RoleBasedFieldSecurityPolicy(PrincipalProvider principalProvider){
        this.principalProvider = principalProvider;
    }

    @Override
    public boolean permitAccess(@NotNull SecureField secureField, @NotNull PropertyWriter propertyWriter, @NotNull Object targey, @Nullable String targetCreatedByUser, @Nullable String currentPrincipalUser) {

        List<String> formattedRoles = Arrays.stream(secureField.roles()).map(role -> formatRole(role)).collect(Collectors.toList());

        int i= 0;
        for(String role : formattedRoles){
            if(!permitRole(role)){
                break;
            }
            i++;
        }

        if(secureField.roleLogic() == EvalulationLogic.AND){
            return i == formattedRoles.size();
        }

        if(secureField.roleLogic() == EvalulationLogic.OR){
            return i > 0;
        }

        if(secureField.roleLogic() == EvalulationLogic.XOR){
            return i == 1;
        }

        return false;
    }

    private boolean permitRole(String role){
        if(principalProvider.getRoles() == null){
            return  false;
        }
        log.debug("Roles is : {}",role);
        log.debug("principalProvider.getRoles() is : {}",principalProvider.getRoles());
        return principalProvider.getRoles().contains(role);
    }

    private String formatRole (String role){
        if(role.startsWith("ROLE_"))
            return role;
        else
            return "ROLE_" + role;
    }

}
