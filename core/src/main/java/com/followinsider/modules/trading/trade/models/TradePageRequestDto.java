package com.followinsider.modules.trading.trade.models;

import com.followinsider.common.models.dtos.PageRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TradePageRequestDto(

        @Valid
        @NotNull
        PageRequestDto pageRequest,

        @Valid
        TradeFiltersDto tradeFilters

) {}