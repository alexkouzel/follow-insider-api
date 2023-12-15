package com.followinsider.secapi.forms.f345;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class OwnershipDoc {

    private OwnershipForm ownershipForm;

    private String txtUrl;

    private String xmlUrl;

    private String accNum;

    private String submissionType;

    private LocalDate reportedAt;

    private LocalDate filedAt;

    private LocalDate updatedAt;

    private int docCount;

}
