package com.followinsider.parsing.f345.footnote;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FootnoteID {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

}
