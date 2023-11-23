package com.followinsider.secapi.form.ref.daily;

import com.followinsider.secapi.form.FormType;
import com.followinsider.secapi.form.ref.FormRef;
import com.followinsider.secapi.form.ref.FormRefParser;
import com.followinsider.util.DateUtils;
import com.followinsider.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyFeedParser implements FormRefParser<String> {

    @Override
    public List<FormRef> parse(String data) throws ParseException {
        String hr = StringUtils.substring(data, "<hr>", "<hr>");
        if (hr == null) throw new ParseException("<hr> field is missing", -1);

        List<FormRef> refs = new ArrayList<>();
        for (String entry : hr.split("\n")) {
            refs.add(parseEntry(entry));
        }
        return refs;
    }

    private FormRef parseEntry(String entry) throws ParseException {
        String[] parts = entry.split("</a>");

        // Parse issuer CIK
        int issuerCikIdx = parts[1].indexOf(">") + 1;
        String issuerCik = parts[1].substring(issuerCikIdx);

        // Parse accession number
        int accNoIdx = parts[0].indexOf(issuerCik) + issuerCik.length() + 1;
        String accNo = parts[0].substring(accNoIdx, parts[0].indexOf("-index"));

        // Parse form type
        int typeIdx = parts[0].indexOf(">") + 1;
        String typeValue = parts[0].substring(typeIdx);
        FormType type = FormType.ofValue(typeValue);

        // Parse filing date
        String dateValue = entry.substring(0, entry.indexOf(" "));
        Date filedAt = DateUtils.parse(dateValue, "MM-dd-yyyy");

        return new FormRef(accNo, issuerCik, type, filedAt);
    }

}
