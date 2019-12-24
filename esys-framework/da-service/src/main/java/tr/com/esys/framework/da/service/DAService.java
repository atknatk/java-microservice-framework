package tr.com.esys.framework.da.service;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.da.dto.DocumentDto;
import com.esys.framework.da.entity.Document;

import java.io.InputStream;
import java.nio.file.Path;

public interface DAService {

    Document add(Company company, User user, String fileName, InputStream inputStream);

    Document add(Company company, Document document);

    Path calculatePath(long companyId, long documentId);

    DocumentDto get(long documentId);

}
