package com.followinsider.secapi.forms.f345.footnote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.common.utils.DateUtils;
import com.followinsider.secapi.forms.des.EdgarDateDeserializer;
import lombok.Getter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import lombok.Setter;

@Getter
@Setter
public class FootnoteEdgarDate {

    private FootnoteID footnoteId;

    @JsonDeserialize(using = EdgarDateDeserializer.class)
    private LocalDate value;

    @JsonCreator
    public FootnoteEdgarDate(@JsonProperty("footnoteId") FootnoteID footnoteId,
                             @JsonProperty("value") LocalDate value) {
        this.footnoteId = footnoteId;
        this.value = value;
    }

    public FootnoteEdgarDate(String value) {
        this.value = DateUtils.parse(value, "yyyy-MM-dd");
    }

}
