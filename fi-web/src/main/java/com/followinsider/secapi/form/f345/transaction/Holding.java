package com.followinsider.secapi.form.f345.transaction;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.followinsider.secapi.form.f345.footnote.FootnoteValue;
import lombok.Data;

@Data
public class Holding {

    private FootnoteValue<String> securityTitle;

    private PostTransactionAmounts postTransactionAmounts;

    private OwnershipNature ownershipNature;

    private TransactionCoding transactionCoding;

    @Data
    public static class TransactionCoding {

        private String transactionFormType;

        @JacksonXmlProperty(isAttribute = true)
        private int footnoteId;

    }

}
