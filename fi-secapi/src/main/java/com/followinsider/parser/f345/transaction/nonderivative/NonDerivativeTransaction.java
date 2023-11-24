package com.followinsider.parser.f345.transaction.nonderivative;

import com.followinsider.parser.f345.transaction.Transaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NonDerivativeTransaction extends Transaction {

    private NonDerivativeTransactionAmounts transactionAmounts;

}
