package com.followinsider.modules.trading.trade.models;

import com.followinsider.modules.trading.form.models.FormDto;

import java.time.LocalDate;

public record TradeDto(

        String securityTitle,

        Double shareCount,

        Double sharePrice,

        Double sharesLeft,

        Double valueLeft,

        LocalDate executedAt,

        TradeType type,

        FormDto form

) {}
