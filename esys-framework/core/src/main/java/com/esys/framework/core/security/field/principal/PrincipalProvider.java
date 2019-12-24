package com.esys.framework.core.security.field.principal;

import javax.annotation.Nullable;
import java.util.List;

public interface PrincipalProvider {

    @Nullable
    String getPrincipal();

    @Nullable
    List<String> getRoles();

    @Nullable
    List<String> getAuthorities();
}
