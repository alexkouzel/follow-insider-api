package com.followinsider.parser.f345.footnote;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FootnoteContainer {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Footnote> footnote;

}
