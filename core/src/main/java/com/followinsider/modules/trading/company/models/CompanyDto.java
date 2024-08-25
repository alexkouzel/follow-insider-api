package com.followinsider.modules.trading.company.models;

public record CompanyDto(

        int cik,

        String name,

        String ticker,

        String exchange

) {}
