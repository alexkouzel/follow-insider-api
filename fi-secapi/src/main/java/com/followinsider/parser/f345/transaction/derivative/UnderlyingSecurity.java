package com.followinsider.parser.f345.transaction.derivative;

import com.followinsider.parser.f345.footnote.FootnoteValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnderlyingSecurity {

    private FootnoteValue<String> underlyingSecurityTitle;

    private FootnoteValue<Double> underlyingSecurityShares;

    private FootnoteValue<Double> underlyingSecurityValue;

}
