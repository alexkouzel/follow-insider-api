package com.followinsider.secapi.forms.f345;

import java.time.LocalDate;

public record OwnershipDoc(

        String accNum,

        String txtUrl,

        String xmlUrl,

        LocalDate filedAt,

        OwnershipForm ownershipForm

) {}
