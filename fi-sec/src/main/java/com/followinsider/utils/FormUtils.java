package com.followinsider.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FormUtils {

    private final String FORM_URL = "https://www.sec.gov/Archives/edgar/data/%s/%s/%s";

    public String getTxtUrl(String issuerCik, String accNum) {
        return getUrl(issuerCik, accNum, accNum + ".txt");
    }

    /* Only for ownership forms */
    public String getXmlUrl(String issuerCik, String accNum, String filename) {
        return getUrl(issuerCik, accNum, "xslF345X03/" + filename);
    }

    public String getUrl(String issuerCik, String accNum, String filename) {
        String urlAccNum = accNum.replaceAll("-", "");
        return String.format(FORM_URL, issuerCik, urlAccNum, filename);
    }

}
