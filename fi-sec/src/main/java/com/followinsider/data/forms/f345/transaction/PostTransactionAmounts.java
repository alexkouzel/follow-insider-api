package com.followinsider.data.forms.f345.transaction;

import com.followinsider.data.forms.f345.footnote.FootnoteValue;
import lombok.*;

@Getter
@Setter
public class PostTransactionAmounts {

    private FootnoteValue<Double> sharesOwnedFollowingTransaction;

    private FootnoteValue<Double> valueOwnedFollowingTransaction;

}
