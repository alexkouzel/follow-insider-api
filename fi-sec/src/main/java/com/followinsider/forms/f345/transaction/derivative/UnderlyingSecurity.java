package com.followinsider.forms.f345.transaction.derivative;

import com.followinsider.forms.f345.footnote.FootnoteValue;
import lombok.*;

@Getter
@Setter
public class UnderlyingSecurity {

    private FootnoteValue<String> underlyingSecurityTitle;

    private FootnoteValue<Double> underlyingSecurityShares;

    private FootnoteValue<Double> underlyingSecurityValue;

}
