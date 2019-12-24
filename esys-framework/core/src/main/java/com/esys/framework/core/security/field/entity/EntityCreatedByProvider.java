package com.esys.framework.core.security.field.entity;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface EntityCreatedByProvider {
    @Nullable
    String getCreatedBy(@NotNull Object target);

}
