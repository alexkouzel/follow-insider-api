package com.followinsider.parser.f345.transaction.nonderivative;

import com.followinsider.parser.f345.transaction.TransactionAmounts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NonDerivativeTransactionAmounts extends TransactionAmounts {
}
