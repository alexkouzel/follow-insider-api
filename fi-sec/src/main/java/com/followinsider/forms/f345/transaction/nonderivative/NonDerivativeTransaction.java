package com.followinsider.forms.f345.transaction.nonderivative;

import com.followinsider.forms.f345.transaction.Transaction;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NonDerivativeTransaction extends Transaction {

    private NonDerivativeTransactionAmounts transactionAmounts;

}
