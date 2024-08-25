package com.followinsider.modules.trading.trade.models;

import java.time.LocalDate;

public record TradeFiltersDto(

        String companyName,

        String companyCik,

        LocalDate executedAt,

        LocalDate filedAt,

        TradeType type

) {}
