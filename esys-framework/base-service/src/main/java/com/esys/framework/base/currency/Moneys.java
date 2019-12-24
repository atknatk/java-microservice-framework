package com.esys.framework.base.currency;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public enum Moneys {
    US_DOLLAR(0),
    AUSTRALIAN_DOLLAR(1),
    DANISH_KRONE(2),
    EURO(3),
    POUND_STERLING(4),
    SWISS_FRANK(5),
    SWEDISH_KRONA(6),
    CANADIAN_DOLLAR(7),
    KUWAITI_DINAR(8),
    NORWEGIAN_KRONE(9),
    SAUDI_RIYAL(10),
    JAPENESE_YEN(11),
    BULGARIAN_LEV(12),
    NEW_LEU(13),
    RUSSIAN_ROUBLE(14),
    IRANIAN_RIAL(15),
    CHINESE_RENMINBI(16),
    PAKISTANI_RUPEE(17),
    QATARI_RIAL(18),
    TURKISH_TL(19);


    int value;
    Moneys(int i){
        value = i;
    }
}
