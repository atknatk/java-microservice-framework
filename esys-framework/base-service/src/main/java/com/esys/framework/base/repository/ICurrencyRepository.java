package com.esys.framework.base.repository;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Repository
@Transactional
public interface ICurrencyRepository extends CrudRepository<Currency,Long> {

    boolean existsCurrencyByDate(Date date);

    List<Currency> getCurrencyByDate(Date date);

    Optional<Currency> getCurrencyByDateAndName(Date date, Moneys name);
}
