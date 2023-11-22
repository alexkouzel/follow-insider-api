package com.followinsider.secapi.forms.f345.transaction.derivative;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DerivativeTable {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DerivativeTransaction> derivativeTransaction;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DerivativeHolding> derivativeHolding;

}
