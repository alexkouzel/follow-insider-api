package com.followinsider.parsing.f345.transaction.derivative;

import com.followinsider.parsing.f345.footnote.FootnoteEdgarDate;
import com.followinsider.parsing.f345.footnote.FootnoteValue;
import com.followinsider.parsing.f345.transaction.Holding;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DerivativeHolding extends Holding {

    private FootnoteValue<Double> conversionOrExercisePrice;

    private FootnoteEdgarDate exerciseDate;

    private FootnoteEdgarDate expirationDate;

    private UnderlyingSecurity underlyingSecurity;

}