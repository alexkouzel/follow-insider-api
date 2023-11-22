package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.common.Color;
import com.followinsider.secapi.forms.f345.owner.Issuer;
import com.followinsider.secapi.utils.StringUtils;
import com.followinsider.secapi.utils.TerminalUtils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class InsiderForm {

    private String accNo;

    private Issuer issuer;

    private Insider insider;

    private List<InsiderTrade> trades;

    private Date reportedAt;

    private String txtUrl;

    private String xmlUrl;

    public List<String> getPrettyTrades() {
        List<String> values = new ArrayList<>();
        String format = "%s at %s %s shares for $%s on %s";

        for (InsiderTrade trade : trades) {
            String moneyValue = StringUtils.stringify(trade.getShareNum() * trade.getSharePrice());
            String dateValue = StringUtils.stringify(trade.getExecutedAt());

            String typeValue = trade.getType() == InsiderTradeType.BUY
                    ? TerminalUtils.color("bought", Color.GREEN)
                    : TerminalUtils.color("sold", Color.RED);

            values.add(String.format(format,
                    insider.getName(),
                    issuer.getIssuerName(),
                    typeValue,
                    moneyValue,
                    dateValue));
        }
        return values;
    }

}
