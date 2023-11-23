package com.followinsider.secapi.form.ref.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkValue {

    @JacksonXmlProperty(isAttribute = true)
    private String rel;

    @JacksonXmlProperty(isAttribute = true)
    private String href;

    @JacksonXmlProperty(isAttribute = true)
    private String type = "text/css";

}
