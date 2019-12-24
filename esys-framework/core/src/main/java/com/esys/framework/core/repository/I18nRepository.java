package com.esys.framework.core.repository;


import com.esys.framework.core.entity.internationalisation.I18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface I18nRepository extends JpaRepository<I18n, Long> {


    @Query("select l.code from I18n l where l.code like ?1%")
    List<I18n> findByI18n(String startWith);
}
