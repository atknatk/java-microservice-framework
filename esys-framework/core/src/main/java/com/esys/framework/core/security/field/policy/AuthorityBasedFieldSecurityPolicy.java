package com.esys.framework.core.security.field.policy;

import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.principal.PrincipalProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthorityBasedFieldSecurityPolicy implements FieldSecurityPolicy {

    private PrincipalProvider principalProvider;

    public AuthorityBasedFieldSecurityPolicy(PrincipalProvider principalProvider){
        this.principalProvider = principalProvider;
    }

    public AuthorityBasedFieldSecurityPolicy(){
    }

    @Override
    public boolean permitAccess(@NotNull SecureField secureField, @NotNull PropertyWriter propertyWriter, @NotNull Object targey, @Nullable String targetCreatedByUser, @Nullable String currentPrincipalUser) {

        List<String> authorities = Arrays.stream(secureField.authorities()).collect(Collectors.toList());

        int i= 0;
        for(String role : authorities){
            if(!permitAuthorities(role)){
                break;
            }
            i++;
        }

        if(secureField.roleLogic() == EvalulationLogic.AND){
            return i == authorities.size();
        }

        if(secureField.roleLogic() == EvalulationLogic.OR){
            return i > 0;
        }

        if(secureField.roleLogic() == EvalulationLogic.XOR){
            return i == 1;
        }

        return false;
    }

    private boolean permitAuthorities(String role){
        if(principalProvider == null || principalProvider.getAuthorities() == null){
            return SecurityUtils.getCurrentUserAuthorities().contains(role);
        }
        log.debug("Authority is : {}",role);
        log.debug("principalProvider.getRoles() is : {}",principalProvider.getAuthorities());
        return principalProvider.getAuthorities().contains(role);
    }

}
