package com.followinsider.secapi.forms.refs.index;

import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexForm {

    private String accNo;

    private String issueCik;

    private String issueName;

    private FormType type;

    private Date filedAt;

}
