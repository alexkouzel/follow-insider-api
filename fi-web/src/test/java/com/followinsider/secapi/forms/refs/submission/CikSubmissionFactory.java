package com.followinsider.secapi.forms.refs.submission;

import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CikSubmissionFactory {

    public static CikSubmission build() throws ParseException {
        return CikSubmission.builder()
                .formerNames(formerNames())
                .addresses(addresses())
                .filings(filings())
                .exchanges(List.of("Nasdaq"))
                .tickers(List.of("AAPL"))
                .sic("3571")
                .sicDescription("Electronic Computers")
                .cik("320193")
                .name("Apple Inc.")
                .category("Large accelerated filer")
                .entityType("operating")
                .phone("(408) 996-1010")
                .fiscalYearEnd("0930")
                .stateOfIncorporationDescription("CA")
                .stateOfIncorporation("CA")
                .insiderTransactionForIssuerExists(true)
                .insiderTransactionForOwnerExists(false)
                .ein(942404110)
                .investorWebsite("")
                .description("")
                .website("")
                .flags("")
                .build();
    }

    private static Map<String, CikSubmission.Address> addresses() {
        return Map.of(
                "mailing", new CikSubmission.Address("ONE APPLE PARK WAY", null, "CUPERTINO", "CA", "95014", "CA"),
                "business", new CikSubmission.Address("ONE APPLE PARK WAY", null, "CUPERTINO", "CA", "95014", "CA"));
    }

    private static List<CikSubmission.FormerName> formerNames() throws ParseException {
        return List.of(
                formerName("APPLE INC", "2007-01-10T00:00:00.000Z", "2019-08-05T00:00:00.000Z"),
                formerName("APPLE COMPUTER INC", "1994-01-26T00:00:00.000Z", "2007-01-04T00:00:00.000Z"),
                formerName("APPLE COMPUTER INC/ FA", "1997-07-28T00:00:00.000Z", "1997-07-28T00:00:00.000Z"));
    }

    private static List<CikSubmission.FilingFile> files() throws ParseException {
        return List.of(file("CIK0000320193-submissions-001.json", "1994-01-26", "2013-02-07", 988));
    }

    private static CikSubmission.Filings filings() throws ParseException {
        CikSubmissionFormData recent = CikSubmissionFormData.builder()
                .accessionNumber(List.of("0000320193-23-000079"))
                .filingDate(List.of(DateUtils.parse("2023-08-08", "yyyy-MM-dd")))
                .reportDate(List.of(DateUtils.parse("2023-08-05", "yyyy-MM-dd")))
                .acceptanceDateTime(List.of(DateUtils.parse("2023-08-08T18:30:28.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
                .primaryDocument(List.of("xslF345X05/wk-form4_1691533817.xml"))
                .primaryDocDescription(List.of("FORM 4"))
                .form(List.of("4"))
                .fileNumber(List.of(""))
                .filmNumber(List.of(""))
                .items(List.of(""))
                .act(List.of(""))
                .size(List.of(10893))
                .isInlineXBRL(List.of(false))
                .isXBRL(List.of(false))
                .build();
        return new CikSubmission.Filings(recent, files());
    }

    private static CikSubmission.FilingFile file(String name, String fromValue,
                                                 String toValue, int size) throws ParseException {
        String format = "yyyy-MM-dd";
        Date from = DateUtils.parse(fromValue, format);
        Date to = DateUtils.parse(toValue, format);
        return new CikSubmission.FilingFile(name, from, to, size);
    }

    private static CikSubmission.FormerName formerName(String name, String fromValue, String toValue) throws ParseException {
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Date from = DateUtils.parse(fromValue, format);
        Date to = DateUtils.parse(toValue, format);
        return new CikSubmission.FormerName(name, from, to);
    }

}
