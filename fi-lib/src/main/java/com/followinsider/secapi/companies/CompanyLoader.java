package com.followinsider.secapi.companies;

import com.followinsider.common.utils.StringUtils;
import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.client.EdgarClient;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompanyLoader {

    private static final String URL = "https://www.sec.gov/files/company_tickers_exchange.json";

    private final DataClient client;

    public CompanyLoader() {
        this.client = new EdgarClient();
    }

    public List<Company> loadCompanies() throws IOException {
        CompanyTickersExchange data = client.loadJsonType(URL, CompanyTickersExchange.class);

        return data.data().stream()
                .map(fields -> {
                    String id = StringUtils.padLeft(fields[0], 10, '0');
                    String name = fields[1];
                    String symbol = fields[2];
                    String exchange = fields[3];
                    return new Company(id, name, symbol, exchange);
                })
                .collect(Collectors.toList());
    }

    private record CompanyTickersExchange(

            String[] fields,

            List<String[]> data

    ) {}

}
