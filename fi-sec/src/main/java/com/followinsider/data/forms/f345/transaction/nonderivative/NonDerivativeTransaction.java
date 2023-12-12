package com.followinsider.data.forms.f345.transaction.nonderivative;

import com.followinsider.data.forms.f345.transaction.Transaction;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NonDerivativeTransaction extends Transaction {

    private NonDerivativeTransactionAmounts transactionAmounts;

}
