package com.esys.framework.core.security.field.policy;

import com.esys.framework.core.security.field.SecureField;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface FieldSecurityPolicy {
    boolean permitAccess(@NotNull SecureField secureField,
                         @NotNull PropertyWriter propertyWriter,
                         @NotNull Object target,
                         @Nullable String targetCreatedByUser,
                         @Nullable String currentPrincipalUser);
}
