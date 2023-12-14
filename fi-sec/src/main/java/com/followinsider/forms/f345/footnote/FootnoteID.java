package com.followinsider.forms.f345.footnote;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootnoteID {

        @JacksonXmlProperty(isAttribute = true)
        private String id;
}