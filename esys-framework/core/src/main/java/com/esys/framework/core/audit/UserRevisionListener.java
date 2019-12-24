package com.esys.framework.core.audit;

import com.esys.framework.core.entity.core.UserRevision;
import com.esys.framework.core.security.SecurityUtils;
import org.hibernate.envers.RevisionListener;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        UserRevision auditEnversInfo = (UserRevision) revisionEntity;
        auditEnversInfo.setUser(SecurityUtils.getCurrentUserLogin());
    }

}