package com.esys.framework.base.currency;

import java.util.Date;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface ICurrency {

    /**
     *
     * @return String current Date
     */
    Date getDate();

    /**
     *
     * @return String Money Name
     */
    String getName();

    /**
     *
     * @return float Buying Price
     */
    float getBuyingPrice();

    /**
     *
     * @return float Selling Price
     */
    float getSellingPrice();

    /**
     *
     * @return boolean is Forex
     */
    boolean isForex();



}
