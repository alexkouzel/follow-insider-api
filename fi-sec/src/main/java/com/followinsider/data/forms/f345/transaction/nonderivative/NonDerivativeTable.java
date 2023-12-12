package com.followinsider.data.forms.f345.transaction.nonderivative;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class NonDerivativeTable {

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<NonDerivativeTransaction> nonDerivativeTransaction;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<NonDerivativeHolding> nonDerivativeHolding;

}
