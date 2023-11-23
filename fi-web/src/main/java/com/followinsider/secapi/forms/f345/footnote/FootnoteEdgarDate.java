package com.followinsider.secapi.forms.f345.footnote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.secapi.forms.deserializers.EdgarDateDeserializer;
import com.followinsider.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.util.Date;

@Data
@NoArgsConstructor
public class FootnoteEdgarDate {

    private FootnoteID footnoteId;

    @JsonDeserialize(using = EdgarDateDeserializer.class)
    private Date value;

    @JsonCreator
    public FootnoteEdgarDate(@JsonProperty("footnoteId") FootnoteID footnoteId,
                             @JsonProperty("value") Date value) {
        this.footnoteId = footnoteId;
        this.value = value;
    }

    public FootnoteEdgarDate(String value) throws ParseException {
        this.value = DateUtils.parse(value, "yyyy-MM-dd");
    }

}
