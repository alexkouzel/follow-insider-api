package com.followinsider.secapi.forms.f345.transaction;

import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import lombok.*;

@Getter
@Setter
public class OwnershipNature {

    private FootnoteValue<String> directOrIndirectOwnership;

    private FootnoteValue<String> natureOfOwnership;

}
