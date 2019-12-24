package com.esys.framework.core.service.impl;

import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.entity.internationalisation.I18n;
import com.esys.framework.core.repository.I18nRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Configurable
@Slf4j
public class I18nService {

    @Autowired
    private I18nRepository repository;

    @PersistenceContext
    private EntityManager em;


    @NoLogging
    public Map<String, String> findUiByLanguage(String lang){
        Map<String, String> results = new HashMap<String, String>();
        if(lang.equalsIgnoreCase("id")||lang.equalsIgnoreCase("code"))
            return results;
        try {
            I18n.class.getDeclaredField(lang);
        } catch (NoSuchFieldException e) {
            log.warn("lang : "+lang+" not found");
            return results;
        }


        String jpaQuery = "SELECT i.code, i."+lang+" FROM I18n i where " +
                "i.code like :prefix||'%' " +
                "or i.code like :prefix2||'%'";
        List<Object[]> resultList = em.createQuery(jpaQuery)
                .setParameter("prefix","ui.")
                .setParameter("prefix2","authority.")
                .getResultList();
        for (Object[] borderTypes: resultList) {
            results.put((String)borderTypes[0], (String)borderTypes[1]);
        }
        return results;
    }

    public void save(I18n i18n){
        try{
            repository.save(i18n);
        }catch (Exception ex){
        }
    }
}
