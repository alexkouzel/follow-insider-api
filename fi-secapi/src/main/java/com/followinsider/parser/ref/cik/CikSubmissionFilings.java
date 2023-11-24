package com.followinsider.parser.ref.cik;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.parser.des.BooleanDeserializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CikSubmissionFilings {

    private Recent recent;

    private List<CikSubmission.FilingFile> files;

    @Data
    public static class Recent {

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
