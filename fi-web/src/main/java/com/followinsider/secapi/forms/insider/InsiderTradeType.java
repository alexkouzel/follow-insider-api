package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.forms.f345.transaction.TransactionCode;

public enum InsiderTradeType {

    BUY, SELL, OTHER;

    public static InsiderTradeType fromTransactionCode(TransactionCode code) {
        return switch (code) {
            case PURCHASE -> InsiderTradeType.BUY;
            case SALE -> InsiderTradeType.SELL;
            default -> InsiderTradeType.OTHER;
        };
    }

}
