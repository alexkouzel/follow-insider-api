package com.followinsider.secapi.forms.f345.transaction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.followinsider.secapi.common.deserializers.BooleanDeserializer;
import com.followinsider.secapi.forms.f345.footnote.FootnoteEdgarDate;
import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;

@Data
@SuperBuilder
@NoArgsConstructor
public class Transaction {

    private FootnoteValue<String> securityTitle;

    private FootnoteValue<String> transactionTimeliness;

    private PostTransactionAmounts postTransactionAmounts;

    private FootnoteEdgarDate transactionDate;
    
    private FootnoteEdgarDate deemedExecutionDate;

    private OwnershipNature ownershipNature;

    private Coding transactionCoding;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Coding {

        private String transactionFormType;

        @JsonDeserialize(using = TransactionCodeDeserializer.class)
        private TransactionCode transactionCode;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private boolean equitySwapInvolved;

        @JacksonXmlProperty(isAttribute = true)
        private Integer footnoteId;
    
    }

    private static class TransactionCodeDeserializer extends JsonDeserializer<TransactionCode> {

        @Override
        public TransactionCode deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
            return TransactionCode.ofValue(parser.getValueAsString());
        }
    
    }

}
