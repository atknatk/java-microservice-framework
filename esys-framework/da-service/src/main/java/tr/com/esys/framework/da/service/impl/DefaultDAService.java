package tr.com.esys.framework.da.service.impl;

import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.da.dto.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.esys.framework.da.dao.DocumentRepository;
import tr.com.esys.framework.da.dao.DocumentVersionRepository;
import com.esys.framework.da.entity.Document;
import tr.com.esys.framework.da.service.DAService;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
@Service
@Transactional
public class DefaultDAService implements DAService {

    private static final String SAFE_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentVersionRepository documentVersionRepository;

    @Override
    @Transactional
    public Document add(Company company, User user, String fileName, InputStream inputStream) {
        Document document = new Document();
        document.setCompany(company);
        document.setFileName(fileName);
        document = documentRepository.save(document);

        return document;
    }

    @Override
    public Document add(Company company, Document document) {
        return null;
    }

    private void saveOnFileSystem(long companyId, long documentId, InputStream inputStream) {

    }

    private OutputStream readOnFileSystem(long companyId, long documentId) {
        return null;
    }

    @Override
    public Path calculatePath(long companyId, long documentId) {
        int LOOPCOUNTER = 0;
        boolean done = false;
        StringBuilder documentkey = new StringBuilder();
        long dividend = documentId;
        long remainder = documentId;
        while (dividend > SAFE_CHARACTERS.length()) {
            //quotient
            remainder = dividend % SAFE_CHARACTERS.length();
            documentkey.append(SAFE_CHARACTERS.indexOf((int)remainder));
            dividend = documentId / SAFE_CHARACTERS.length();
        }
        documentkey.append(SAFE_CHARACTERS.indexOf((int)remainder));
        return Paths.get(Long.toString(companyId), documentkey.toString());
    }

    @Override
    public DocumentDto get(long documentId) {
        return null;
    }
}
