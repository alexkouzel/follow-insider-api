package com.followinsider.modules.trading.trade.models;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record TradeFilters(

    String companyCik,

    LocalDate executedAt,

    @PastOrPresent
    LocalDate filedAt,

    TradeType type

) {}
