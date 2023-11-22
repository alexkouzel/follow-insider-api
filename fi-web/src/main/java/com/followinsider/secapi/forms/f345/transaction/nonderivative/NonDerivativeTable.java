package com.followinsider.secapi.forms.f345.transaction.nonderivative;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonDerivativeTable {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<NonDerivativeTransaction> nonDerivativeTransaction;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<NonDerivativeHolding> nonDerivativeHolding;

}
