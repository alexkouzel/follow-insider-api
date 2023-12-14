package com.followinsider.forms.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.forms.des.EdgarDateDeserializer;
import lombok.*;

import java.util.Date;

@Getter
@Setter
public class Signature {

        private String signatureName;

        @JsonDeserialize(using = EdgarDateDeserializer.class)
        private Date signatureDate;

}