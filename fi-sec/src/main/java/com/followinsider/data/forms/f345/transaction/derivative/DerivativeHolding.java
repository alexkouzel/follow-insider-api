package com.followinsider.data.forms.f345.transaction.derivative;

import com.followinsider.data.forms.f345.footnote.FootnoteEdgarDate;
import com.followinsider.data.forms.f345.footnote.FootnoteValue;
import com.followinsider.data.forms.f345.transaction.Holding;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DerivativeHolding extends Holding {

    private FootnoteValue<Double> conversionOrExercisePrice;

    private FootnoteEdgarDate exerciseDate;

    private FootnoteEdgarDate expirationDate;

    private UnderlyingSecurity underlyingSecurity;

}
