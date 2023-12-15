package com.followinsider.secapi.forms;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class FormUrlParser {

    private final String FORM_URL = "https://www.sec.gov/Archives/edgar/data/%s/%s/%s";

    private final Pattern DASH_PATTERN = Pattern.compile("-");

    public String getTxtUrl(String issuerCik, String accNum) {
        return getUrl(issuerCik, accNum, accNum + ".txt");
    }

    /* Only valid for ownership forms */
    public String getXmlUrl(String issuerCik, String accNum, String filename) {
        return getUrl(issuerCik, accNum, "xslF345X03/" + filename);
    }

    private String getUrl(String issuerCik, String accNum, String filename) {
        String urlAccNum = DASH_PATTERN.matcher(accNum).replaceAll("");
        return String.format(FORM_URL, issuerCik, urlAccNum, filename);
    }

}
