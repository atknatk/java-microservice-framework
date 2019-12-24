package com.esys.framework.core.repository;


import com.esys.framework.core.entity.core.AppConfiguration;
import com.esys.framework.core.entity.internationalisation.I18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAppConfigurationRepository extends JpaRepository<AppConfiguration, Long> {

    @Transactional(readOnly = true)
    Optional<AppConfiguration> getFirstByKeyAndValue(String key, String value);

    @Transactional(readOnly = true)
    Optional<AppConfiguration> getFirstByKey(String key);

    @Transactional(readOnly = true)
    List<AppConfiguration> getAllByKey(String key);
}
