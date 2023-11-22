package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormRef {

    private String accNo;

    private String issueCik;

    private FormType type;

    private Date filedAt;

}
