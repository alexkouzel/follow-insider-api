package com.followinsider.secapi.form;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FormUtils {

    private static final String FORM_URL = "https://www.sec.gov/Archives/edgar/data/%s/%s/%s";

    public String getTxtUrl(String issuerCik, String accNo) {
        return getUrl(issuerCik, accNo, accNo + ".txt");
    }

    /* Only for ownership forms */
    public String getXmlUrl(String issuerCik, String accNo, String filename) {
        return getUrl(issuerCik, accNo, "xslF345X03/" + filename);
    }

    public String getUrl(String issueCik, String accNo, String filename) {
        String urlAccNo = accNo.replaceAll("-", "");
        return String.format(FORM_URL, issueCik, urlAccNo, filename);
    }

}
