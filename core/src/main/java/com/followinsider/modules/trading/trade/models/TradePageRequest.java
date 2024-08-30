package com.followinsider.modules.trading.trade.models;

import com.followinsider.common.models.requests.GetPageRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TradePageRequest(

    @Valid
    @NotNull
    GetPageRequest getPageRequest,

    @Valid
    TradeFilters tradeFilters

) {}