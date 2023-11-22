package com.followinsider.secapi.forms.f345.transaction.derivative;

import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import com.followinsider.secapi.forms.f345.transaction.TransactionAmounts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DerivativeTransactionAmounts extends TransactionAmounts {

    private FootnoteValue<Double> transactionTotalValue;

}
