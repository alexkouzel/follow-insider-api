package com.followinsider.secapi.forms.refs.daily;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.utils.CollectionUtils;
import com.followinsider.secapi.utils.DateUtils;
import com.followinsider.secapi.utils.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyFeedParser {

    public static List<FormRef> parseRefs(String data) throws ParseException {
        DailyFeed feed = parseFeed(data);
        if (feed == null) return null;

        return CollectionUtils.map(feed.getForms(),
                form -> new FormRef(
                        form.getAccNo(),
                        form.getIssuerCik(),
                        form.getType(),
                        form.getFiledAt()
                ));
    }

    public static DailyFeed parseFeed(String data) throws ParseException {
        String hr = StringUtils.substring(data, "<hr>", "<hr>");
        if (hr == null) return null;

        List<DailyForm> forms = new ArrayList<>();
        for (String entry : hr.split("\n")) {
            DailyForm form = parseEntry(entry);
            forms.add(form);
        }
        return new DailyFeed(forms);
    }

    private static DailyForm parseEntry(String entry) throws ParseException {
        String[] parts = entry.split("</a>");

        // Issuer details
        int issuerCikIdx = parts[1].indexOf(">") + 1;
        String issuerCik = parts[1].substring(issuerCikIdx);
        String issuerName = parts[2].trim();

        // Accession number
        int accNoIdx = parts[0].indexOf(issuerCik) + issuerCik.length() + 1;
        String accNo = parts[0].substring(accNoIdx, parts[0].indexOf("-index"));

        // Form type
        int typeIdx = parts[0].indexOf(">") + 1;
        String typeValue = parts[0].substring(typeIdx);
        FormType type = FormType.ofValue(typeValue);

        // Filing date
        String dateValue = entry.substring(0, entry.indexOf(" "));
        Date filedAt = DateUtils.parse(dateValue, "MM-dd-yyyy");

        return new DailyForm(accNo, issuerCik, issuerName, type, filedAt);
    }

}
