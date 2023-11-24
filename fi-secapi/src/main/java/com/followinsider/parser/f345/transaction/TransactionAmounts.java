package com.followinsider.parser.f345.transaction;

import com.followinsider.parser.f345.footnote.FootnoteValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TransactionAmounts {

    private FootnoteValue<Double> transactionShares;

    private FootnoteValue<Double> transactionPricePerShare;

    private FootnoteValue<String> transactionAcquiredDisposedCode;

}
