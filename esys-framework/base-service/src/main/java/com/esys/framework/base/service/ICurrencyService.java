package com.esys.framework.base.service;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.entity.Currency;

import java.util.Date;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface ICurrencyService {

    Currency getPrice(Moneys moneys);

    List<Currency> today();

    void retriveCurrency();

    List<Currency> byDate(Date date);

}
