package com.followinsider.data.forms.refs.index;

import com.followinsider.data.forms.refs.FormRef;
import com.followinsider.data.forms.refs.FormType;
import com.followinsider.utils.DateUtils;
import com.followinsider.utils.StringUtils;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UtilityClass
public class IndexFeedParser {

    public List<FormRef> parse(InputStream stream) throws ParseException {
        List<FormRef> refs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            int idx = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (idx >= 11) refs.add(parseLine(line));
                idx++;
            }
        } catch (IOException e) {
            throw new ParseException("Failed to read a line", -1);
        }
        return refs;
    }

    private FormRef parseLine(String line) throws ParseException {
        String[] parts = line.split("\\|");

        if (parts.length != 5)
            throw new ParseException("Invalid number of data parts", -1);

        // Parse accession number
        int accNumIdx = parts[4].lastIndexOf("/") + 1;
        String accNum = parts[4].substring(accNumIdx, parts[4].length() - 4);

        // Parse issuer CIK
        String issuerCik = parts[0];
        issuerCik = StringUtils.pad(issuerCik, 10, '0');

        // Parse form type
        FormType type = FormType.ofValue(parts[2]);

        // Parse filing date
        Date filedAt = DateUtils.parse(parts[3], "yyyy-MM-dd");

        return new FormRef(accNum, issuerCik, type, filedAt);
    }

}
