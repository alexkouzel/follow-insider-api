package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.List;

public class LatestFeedFactory {

    public static LatestFeed build() throws ParseException {
        return LatestFeed.builder()
                .title("Latest Filings")
                .id("https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent")
                .updated(DateUtils.parse("2023-08-15T11:21:52-04:00", "yyyy-MM-dd'T'HH:mm:ssXXX"))
                .author(new LatestFeed.Author("Webmaster", "webmaster@sec.gov"))
                .entry(entries())
                .link(links())
                .build();
    }

    private static List<LinkValue> links() {
        return List.of(
                new LinkValue("alternate", "/cgi-bin/browse-edgar?action=getcurrent", "text/css"),
                new LinkValue("self", "/cgi-bin/browse-edgar?action=getcurrent", "text/css"));
    }

    private static List<LatestFeedEntry> entries() throws ParseException {
        return List.of(entry0());
    }

    private static LatestFeedEntry entry0() throws ParseException {
        return LatestFeedEntry.builder()
                .title("424B2 - CITIGROUP INC (0000831001) (Filer)")
                .id("urn:tag:sec.gov,2008:accession-number=0000950103-23-012038")
                .summary("<b>Filed:</b> 2023-08-15 <b>AccNo:</b> 0000950103-23-012038 <b>Size:</b> 74 KB")
                .category(new LatestFeedEntry.Category("https://www.sec.gov/", "form type", "424B2"))
                .updated(DateUtils.parse("2023-08-15T11:21:11-04:00", "yyyy-MM-dd'T'HH:mm:ssXXX"))
                .link(new LinkValue("alternate", "https://www.sec.gov/Archives/edgar/data/831001/000095010323012038/0000950103-23-012038-index.htm", "text/html"))
                .build();
    }

}
