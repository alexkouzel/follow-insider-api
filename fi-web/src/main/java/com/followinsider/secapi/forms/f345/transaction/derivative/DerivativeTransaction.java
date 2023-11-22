package com.followinsider.secapi.forms.f345.transaction.derivative;

import com.followinsider.secapi.forms.f345.footnote.FootnoteEdgarDate;
import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import com.followinsider.secapi.forms.f345.transaction.Transaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DerivativeTransaction extends Transaction {

    private DerivativeTransactionAmounts transactionAmounts;

    private FootnoteValue<Double> conversionOrExercisePrice;

    private FootnoteEdgarDate exerciseDate;

    private FootnoteEdgarDate expirationDate;

    private UnderlyingSecurity underlyingSecurity;

}
