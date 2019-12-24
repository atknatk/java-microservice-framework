package com.esys.framework.base.currency;

import com.esys.framework.base.entity.Currency;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public final class CurrencyFactory {
    private Moneys money;
    private String currencyName;
    private float currencySellingPrice;
    private float currencyBuyingPrice;
    private boolean isForex;

    private Date retriveDate;
    private Document document;

    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");



    public CurrencyFactory(Date date){
        fetch(date);
    }


    public CurrencyFactory(){
        fetch(null);
    }

    /**
     *  set default money name
     * @param money Enum Moneys
     */
    public void setCurrencies(Moneys money, @Nullable Date date){

        this.money = money;
        parse(date);
    }

    /**
     * <p>return Currency interface for selected money name, buying and selling price</p>
     * @return Currency
     */
    public ICurrency getCurrency(){


        return new ICurrency() {
            @Override
            public Date getDate() {
                return date;
            }

            @Override
            public String getName() {
                return currencyName;
            }

            @Override
            public float getBuyingPrice() {
                return currencyBuyingPrice;
            }

            @Override
            public float getSellingPrice() {
                return currencySellingPrice;
            }

            @Override
            public boolean isForex(){return isForex;}
        };

    }


    private void fetch(@Nullable  Date date){
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new URL(getUrl(date)).openStream());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUrl(@Nullable Date date) {
        if(date == null) return "http://www.tcmb.gov.tr/kurlar/today.xml";

        StringBuilder builder = new StringBuilder("http://www.tcmb.gov.tr/kurlar/");

            builder.append(date.getYear())
                    .append(date.getMonth() < 10 ? "0" : "")
                    .append(date.getMonth())
                    .append("/")
                    .append(date.getDay() < 10 ? "0" : "")
                    .append(date.getDay())
                    .append(date.getMonth() < 10 ? "0" : "")
                    .append(date.getMonth())
                    .append(date.getYear())
                    .append(".xml");
            return builder.toString();
    }




    /**
     * <p>Get source xml file and parse Currency Name, Date, Moneys info</p>
     */
    public Currency parseMoney(Moneys money){
        Currency currency;
        if(money.value == Moneys.TURKISH_TL.value) {
           currency = new Currency();
            currency.setDate(getToday());
            currency.setBuyingPrice(1);
            currency.setSellingPrice(1);
            currency.setForex(false);
            currency.setName(money);
            return currency;
        }

        try {


            // get Date
            String dateStr = document.getDocumentElement().getAttribute("Date");

            currency = new Currency();
            if(dateStr != null && dateStr.length() == 10){
                currency.setDate(sdf.parse(dateStr));
            }



            // get Currency Tag
            NodeList nodeList = document.getDocumentElement().getElementsByTagName("Currency");

            Node node = nodeList.item(money.value);

            if (node.getNodeType() == Node.ELEMENT_NODE){


                Element element = (Element) node;

                String enumMoney = element.getElementsByTagName("CurrencyName").item(0).getTextContent().replace(" ","_");

                currency.setName(Moneys.valueOf(enumMoney));
                // money index > 12 is forex
                if (money.value>12){

                    currency.setBuyingPrice(Float.parseFloat(element.getElementsByTagName("ForexBuying").item(0).getTextContent()));
                    currency.setSellingPrice(Float.parseFloat(element.getElementsByTagName("ForexSelling").item(0).getTextContent()));
                    currency.setForex(true);
                }

                // money index < 12 is normal
                else {

                    String buying = element.getElementsByTagName("BanknoteBuying").item(0).getTextContent();
                    if(!StringUtils.isEmpty(buying)){
                        currency.setBuyingPrice(Float.parseFloat(buying));
                        currency.setSellingPrice(Float.parseFloat(element.getElementsByTagName("BanknoteSelling").item(0).getTextContent()));
                    }else{
                        currency.setBuyingPrice(0f);
                        currency.setSellingPrice(0f);
                    }
                    currency.setForex(false);
                }


            }
            return currency;
        }

        catch (ParseException e) { e.printStackTrace();}
        return null;
    }


    /**
     * <p>Get source xml file and parse Currency Name, Date, Moneys info</p>
     */
    private void parse(Date date){

        try {


            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            /**
             * Connecting <a>http://www.tcmb.gov.tr/kurlar/today.xml</a>
             * and open
             */
            if(retriveDate == null ||  getToday().before(retriveDate) || document == null){
                document = builder.parse(new URL("http://www.tcmb.gov.tr/kurlar/today.xml").openStream());
                this.retriveDate = this.getToday();
            }

            // get Date
            String dateStr = document.getDocumentElement().getAttribute("Date");
            if(dateStr != null && dateStr.length() == 10){
                date = sdf.parse(dateStr);
            }


            // get Currency Tag
            NodeList nodeList = document.getDocumentElement().getElementsByTagName("Currency");

            Node node = nodeList.item(money.value);

            if (node.getNodeType() == Node.ELEMENT_NODE){


                Element element = (Element) node;


                currencyName = element.getElementsByTagName("CurrencyName").item(0).getTextContent();

                // money index > 12 is forex
                if (money.value>12){

                    currencyBuyingPrice = Float.parseFloat(element.getElementsByTagName("ForexBuying").item(0).getTextContent());
                    currencySellingPrice = Float.parseFloat(element.getElementsByTagName("ForexSelling").item(0).getTextContent());
                    isForex=true;
                }

                // money index < 12 is normal
                else {

                    currencyBuyingPrice = Float.parseFloat(element.getElementsByTagName("BanknoteBuying").item(0).getTextContent());
                    currencySellingPrice = Float.parseFloat(element.getElementsByTagName("BanknoteSelling").item(0).getTextContent());
                    isForex =false;
                }


            }

        }

        catch (ParserConfigurationException parse){ parse.printStackTrace(); }
        catch (SAXException e) { e.printStackTrace();}
        catch (IOException e) { e.printStackTrace(); }
        catch (ParseException e) { e.printStackTrace();}
    }





    private Date getToday(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
