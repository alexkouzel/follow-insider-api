package com.followinsider.forms.f345.transaction.derivative;

import com.followinsider.forms.f345.footnote.FootnoteValue;
import com.followinsider.forms.f345.transaction.TransactionAmounts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DerivativeTransactionAmounts extends TransactionAmounts {

    private FootnoteValue<Double> transactionTotalValue;

}
