package com.followinsider.secapi.forms.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.secapi.common.deserializers.EdgarDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signature {
    
    private String signatureName;

    @JsonDeserialize(using = EdgarDateDeserializer.class)
    private Date signatureDate;

}
