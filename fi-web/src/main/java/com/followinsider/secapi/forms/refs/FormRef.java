package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class FormRef {

    private final String accNo;

    private final String issuerCik;

    private final FormType type;

    private final Date filedAt;

}
