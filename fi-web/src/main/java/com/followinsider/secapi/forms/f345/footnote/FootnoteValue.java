package com.followinsider.secapi.forms.f345.footnote;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FootnoteValue<T> {

    private FootnoteID footnoteId;

    private T value;

    public FootnoteValue(T value, String id) {
        this.value = value;
        this.footnoteId = new FootnoteID(id);
    }

    public FootnoteValue(T value) {
        this.value = value;
    }

}
