package com.followinsider.secapi.forms.f345.transaction.derivative;

import com.followinsider.secapi.forms.f345.footnote.FootnoteEdgarDate;
import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import com.followinsider.secapi.forms.f345.transaction.Transaction;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DerivativeTransaction extends Transaction {

    private DerivativeTransactionAmounts transactionAmounts;

    private FootnoteValue<Double> conversionOrExercisePrice;

    private FootnoteEdgarDate exerciseDate;

    private FootnoteEdgarDate expirationDate;

    private UnderlyingSecurity underlyingSecurity;

}
