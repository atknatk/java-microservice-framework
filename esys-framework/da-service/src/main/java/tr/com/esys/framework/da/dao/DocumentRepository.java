package tr.com.esys.framework.da.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.esys.framework.da.entity.Document;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
