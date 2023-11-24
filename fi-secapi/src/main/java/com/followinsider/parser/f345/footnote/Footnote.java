package com.followinsider.parser.f345.footnote;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Footnote {

    @JacksonXmlText
    private String value;

    @JacksonXmlProperty(isAttribute = true)
    private String id;

}