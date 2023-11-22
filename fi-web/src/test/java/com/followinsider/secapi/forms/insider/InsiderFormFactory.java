package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.forms.f345.owner.Issuer;
import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public class InsiderFormFactory {

    private final static String ARCHIVES_URL = "https://www.sec.gov/Archives/edgar/data";

    public static InsiderForm build() throws ParseException {
        return InsiderForm.builder()
                .accNo("0001790565-23-000009")
                .issuer(new Issuer("0001318605", "Tesla, Inc.", "TSLA"))
                .insider(new Insider("0001790565", "Baglino Andrew D", List.of("SVP Powertrain and Energy Eng.")))
                .reportedAt(DateUtils.parse("20230727", "yyyyMMdd"))
                .trades(Collections.singletonList(trade0()))
                .txtUrl(ARCHIVES_URL + "/1318605/000179056523000009/0001790565-23-000009.txt")
                .xmlUrl(ARCHIVES_URL + "/1318605/000179056523000009/xslF345X03/edgardoc.xml")
                .build();
    }

    private static InsiderTrade trade0() throws ParseException {
        return InsiderTrade.builder()
                .securityTitle("Common Stock")
                .shareNum(10500.0)
                .sharePrice(268.23)
                .sharesOwned(66834.25)
                .executedAt(DateUtils.parse("2023-07-27", "yyyy-MM-dd"))
                .type(InsiderTradeType.SELL)
                .build();
    }

}
