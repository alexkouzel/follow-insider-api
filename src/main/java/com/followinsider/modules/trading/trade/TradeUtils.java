package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.form.FormUtils;
import com.followinsider.modules.trading.form.models.FormDto;
import com.followinsider.modules.trading.trade.models.Trade;
import com.followinsider.modules.trading.trade.models.TradeDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TradeUtils {

    public TradeDto toDto(Trade trade, boolean withForm) {
        FormDto form = withForm
                ? FormUtils.toDto(trade.getForm(), false)
                : null;

        return new TradeDto(
                trade.getSecurityTitle(),
                trade.getShareCount(),
                trade.getSharePrice(),
                trade.getSharesLeft(),
                trade.getValueLeft(),
                trade.getExecutedAt(),
                trade.getType(),
                form
        );
    }

}
