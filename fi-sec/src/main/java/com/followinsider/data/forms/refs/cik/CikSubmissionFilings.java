package com.followinsider.data.forms.refs.cik;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.data.forms.des.BooleanDeserializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CikSubmissionFilings {

    private Recent recent;

    private List<CikSubmission.FilingFile> files;

    @Getter
    @Setter
    public class Recent {

        private List<String> accessionNumber;

        private List<String> act;

        private List<String> form;

        private List<String> fileNumber;

        private List<String> filmNumber;

        private List<String> items;

        private List<String> primaryDocument;

        private List<String> primaryDocDescription;

        private List<Date> filingDate;

        private List<Date> reportDate;

        private List<Date> acceptanceDateTime;

        private List<Integer> size;

        @JsonDeserialize(contentUsing = BooleanDeserializer.class)
        private List<Boolean> isXBRL;

        @JsonDeserialize(contentUsing = BooleanDeserializer.class)
        private List<Boolean> isInlineXBRL;

    }

}
