package com.followinsider.data.forms.f345.transaction;

import com.followinsider.data.forms.f345.footnote.FootnoteValue;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class TransactionAmounts {

    private FootnoteValue<Double> transactionShares;

    private FootnoteValue<Double> transactionPricePerShare;

    private FootnoteValue<String> transactionAcquiredDisposedCode;

}
