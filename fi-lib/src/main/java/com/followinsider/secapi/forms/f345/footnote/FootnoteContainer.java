package com.followinsider.secapi.forms.f345.footnote;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class FootnoteContainer {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Footnote> footnote;

}