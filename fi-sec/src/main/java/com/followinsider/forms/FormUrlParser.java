package com.followinsider.forms;

import com.followinsider.forms.refs.FormRef;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FormUrlParser {

    private final String FORM_URL = "https://www.sec.gov/Archives/edgar/data/%s/%s/%s";

    public String getTxtUrl(FormRef ref) {
        return getTxtUrl(ref.issuerCik(), ref.accNum());
    }

    public String getTxtUrl(String issuerCik, String accNum) {
        return getUrl(issuerCik, accNum, accNum + ".txt");
    }

    /* Only valid for ownership forms */
    public String getXmlUrl(String issuerCik, String accNum, String filename) {
        return getUrl(issuerCik, accNum, "xslF345X03/" + filename);
    }

    private String getUrl(String issuerCik, String accNum, String filename) {
        String urlAccNum = accNum.replaceAll("-", "");
        return String.format(FORM_URL, issuerCik, urlAccNum, filename);
    }

}
