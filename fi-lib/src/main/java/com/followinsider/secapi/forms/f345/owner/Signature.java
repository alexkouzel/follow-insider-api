package com.followinsider.secapi.forms.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.secapi.forms.des.EdgarDateDeserializer;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Signature {

    private String signatureName;

    @JsonDeserialize(using = EdgarDateDeserializer.class)
    private LocalDate signatureDate;

}