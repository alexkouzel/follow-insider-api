package com.followinsider.modules.trading.trade.models;

import com.followinsider.modules.trading.form.models.FormDto;

import java.time.LocalDate;

public interface TradeView {

    String getSecurityTitle();

    Double getShareCount();

    Double getSharePrice();

    Double getSharesLeft();

    Double getValueLeft();

    LocalDate getExecutedAt();

    TradeType getType();

    FormDto getForm();

}
