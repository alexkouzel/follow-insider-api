package com.followinsider.modules.trading.trade.models;

import java.time.LocalDate;

public interface BasicTradeView {

    String getSecurityTitle();

    Double getShareCount();

    Double getSharePrice();

    Double getSharesLeft();

    Double getValueLeft();

    LocalDate getExecutedAt();

    TradeType getType();

}
