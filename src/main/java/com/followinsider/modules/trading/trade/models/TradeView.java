package com.followinsider.modules.trading.trade.models;

import java.time.LocalDate;

public interface TradeView {

    String getSecurityTitle();

    Double getShareCount();

    Double getSharePrice();

    LocalDate getExecutedAt();

    Double getSharesLeft();

    Double getValueLeft();

    TradeType getType();

}
