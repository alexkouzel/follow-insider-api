package com.followinsider.forms.f345.footnote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
