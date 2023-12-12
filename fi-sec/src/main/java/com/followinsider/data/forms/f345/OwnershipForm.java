package com.followinsider.data.forms.f345;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.followinsider.data.forms.f345.footnote.FootnoteContainer;
import com.followinsider.data.forms.f345.owner.Issuer;
import com.followinsider.data.forms.f345.owner.ReportingOwner;
import com.followinsider.data.forms.f345.owner.Signature;
import com.followinsider.data.forms.f345.transaction.derivative.DerivativeTable;
import com.followinsider.data.forms.f345.transaction.nonderivative.NonDerivativeTable;
import com.followinsider.data.forms.des.BooleanDeserializer;
import com.followinsider.data.forms.des.EdgarDateDeserializer;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OwnershipForm {

        private String schemaVersion;

        private String documentType;

        private String aff10b5One;

        private NonDerivativeTable nonDerivativeTable;

        private DerivativeTable derivativeTable;

        private Issuer issuer;

        @JsonDeserialize(using = EdgarDateDeserializer.class)
        private Date periodOfReport;

        @JsonDeserialize(using = EdgarDateDeserializer.class)
        private Date dateOfOriginalSubmission;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private Boolean noSecuritiesOwned;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private Boolean notSubjectToSection16;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private Boolean form3HoldingsReported;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private Boolean form4TransactionsReported;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ReportingOwner> reportingOwner;

        private FootnoteContainer footnotes;

        private String remarks;

        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Signature> ownerSignature;

}