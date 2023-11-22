package com.followinsider.secapi.forms.refs.index;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.List;

public class IndexFeedFactory {

    public static IndexFeed build() throws ParseException {
        return IndexFeed.builder()
                .forms(List.of(form0()))
                .description("Master Index of EDGAR Dissemination Feed")
                .comments("webmaster@sec.gov")
                .anonymousFTP("ftp://ftp.sec.gov/edgar/")
                .cloudHTTP("https://www.sec.gov/Archives/")
                .updatedAt(DateUtils.parse("September 30, 2022", "MMMM dd, yyyy"))
                .build();
    }

    private static IndexForm form0() throws ParseException {
        return IndexForm.builder()
                .accNo("0000950170-22-016293")
                .issueCik("1000045")
                .issueName("NICHOLAS FINANCIAL INC")
                .type(FormType.Q10)
                .filedAt(DateUtils.parse("2022-08-10", "yyyy-MM-dd"))
                .build();
    }

}
