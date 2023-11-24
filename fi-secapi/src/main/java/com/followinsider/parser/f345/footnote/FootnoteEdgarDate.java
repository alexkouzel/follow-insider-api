package com.followinsider.parser.f345.footnote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.parser.des.EdgarDateDeserializer;
import com.followinsider.util.DateUtil;
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
        this.value = DateUtil.parse(value, "yyyy-MM-dd");
    }

}
