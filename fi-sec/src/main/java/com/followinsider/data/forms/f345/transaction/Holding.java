package com.followinsider.data.forms.f345.transaction;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.followinsider.data.forms.f345.footnote.FootnoteValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Holding {

    private FootnoteValue<String> securityTitle;

    private PostTransactionAmounts postTransactionAmounts;

    private OwnershipNature ownershipNature;

    private TransactionCoding transactionCoding;

    @Getter
    @Setter
    public static class TransactionCoding {

        String transactionFormType;

        @JacksonXmlProperty(isAttribute = true)
        int footnoteId;

    }

}
