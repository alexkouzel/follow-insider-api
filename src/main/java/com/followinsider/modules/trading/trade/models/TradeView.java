package com.followinsider.modules.trading.trade.models;

import java.time.LocalDate;

public interface TradeView {

    String getSecurityTitle();

    Double getShareNum();

    Double getSharePrice();

    String getSharePriceFootnote();

    LocalDate getExecutedAt();

    Double getSharesLeft();

    TradeType getType();

}
