package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LatestForm {

    private String accNo;

    private String issueCik;

    private String reportingEntity;

    private FormType type;

    private Date filedAt;

}
