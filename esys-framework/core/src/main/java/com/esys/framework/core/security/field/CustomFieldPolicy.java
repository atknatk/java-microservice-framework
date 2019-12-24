package com.esys.framework.core.security.field;

import com.esys.framework.core.consts.FieldSecurityConstants;
import com.esys.framework.core.security.field.policy.FieldSecurityPolicy;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Component(FieldSecurityConstants.CUSTOM_FIELD_POLICY)
public class CustomFieldPolicy implements FieldSecurityPolicy {
    @Override
    @PreAuthorize("hasAuthority('Field')")
    public boolean permitAccess(@NotNull SecureField secureField, @NotNull PropertyWriter propertyWriter, @NotNull Object target, @Nullable String targetCreatedByUser, @Nullable String currentPrincipalUser) {
        return true;
    }
}