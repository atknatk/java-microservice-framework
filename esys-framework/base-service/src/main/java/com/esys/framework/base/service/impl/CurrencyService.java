package com.esys.framework.base.service.impl;

import com.esys.framework.base.currency.CurrencyFactory;
import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.repository.ICurrencyRepository;
import com.esys.framework.base.service.ICurrencyService;
import com.esys.framework.core.aop.logging.NoLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Service
public class CurrencyService implements ICurrencyService {

    @Autowired
    private transient ICurrencyRepository currencyRepository;

    @Override
    public Currency getPrice(Moneys moneys) {
        Optional<Currency> currency = currencyRepository.getCurrencyByDateAndName(getTodayTurkish(),moneys);
        if(!currency.isPresent()){
            retriveCurrency();
        }
        currency = currencyRepository.getCurrencyByDateAndName(getTodayTurkish(),moneys);

        if(currency.isPresent()){
            return currency.get();
        }
        CurrencyFactory currencyApi =  new CurrencyFactory();
        return currencyApi.parseMoney(moneys);
    }

    @Override
    public List<Currency> today() {
        List<Currency> currencyList = currencyRepository.getCurrencyByDate(getTodayTurkish());
        if(!currencyList.isEmpty()) return currencyList
                ;
        CurrencyFactory currencyApi =  new CurrencyFactory();
        for (Moneys money:Moneys.values()) {
            Currency currency = currencyApi.parseMoney(money);
            if(currency != null){
                currencyList.add(currency);
            }
        }
        return currencyList;
    }


    @Override
    public List<Currency> byDate(Date date) {

        List<Currency> currencies = currencyRepository.getCurrencyByDate(date);
        if(!currencies.isEmpty()) return currencies;

        List<Currency> currencyList = new ArrayList();
        CurrencyFactory currencyApi =  new CurrencyFactory(date);

        for (Moneys money:Moneys.values()) {
            Currency currency = currencyApi.parseMoney(money);
            if(currency != null){
                currencyList.add(currency);
            }
        }
        return currencyList;
    }


    @Scheduled(cron = "0 0 0/6 1/1 * ?")
    @NoLogging
    public void retriveCurrency(){
        CurrencyFactory currencyApi =  new CurrencyFactory();

        Currency currency = currencyApi.parseMoney(Moneys.US_DOLLAR);
        // Null ise iptal
        if(currency == null){
            return;
        }

        // Zaten kayıtlı ise iptal
        if(currencyRepository.existsCurrencyByDate(currency.getDate())){
            return;
        }

        List<Currency> currencyList = new ArrayList();


        for (Moneys money:Moneys.values()) {
            currency = currencyApi.parseMoney(money);
            if(currency != null){
                currencyList.add(currency);
            }
        }
        currencyRepository.saveAll(currencyList);

    }


    private Date getTodayTurkish(){
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.HOUR_OF_DAY) <18){
            c.add(Calendar.DATE,-1);
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


}
