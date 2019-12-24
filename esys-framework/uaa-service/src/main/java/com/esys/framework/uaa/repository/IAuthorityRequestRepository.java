package com.esys.framework.uaa.repository;

import com.esys.framework.core.entity.uaa.AuthorityRequest;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface IAuthorityRequestRepository extends DataTablesRepository<AuthorityRequest, Long> {

}
