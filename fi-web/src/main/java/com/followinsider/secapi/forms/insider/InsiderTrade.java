package com.followinsider.secapi.forms.insider;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class InsiderTrade {

    private String securityTitle;

    private Double shareNum;

    private Double sharesOwned;

    private Double sharePrice;

    private String sharePriceFootnote;

    private Date executedAt;

    private InsiderTradeType type;

}
