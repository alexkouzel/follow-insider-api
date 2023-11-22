package com.followinsider.secapi.forms.refs.index;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.utils.CollectionUtils;
import com.followinsider.secapi.utils.DateUtils;
import com.followinsider.secapi.utils.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndexFeedParser {

    private static final int META_INFO_MAX_LINE = 5;

    private static final int FORM_START_LINE = 11;

    public static List<FormRef> parseRefs(InputStream stream) throws IOException {
        List<IndexForm> forms = parseFeed(stream).getForms();
        return CollectionUtils.map(forms,
                form -> new FormRef(
                        form.getAccNo(),
                        form.getIssueCik(),
                        form.getType(),
                        form.getFiledAt()
                ));
    }

    public static IndexFeed parseFeed(InputStream stream) throws IOException {
        IndexFeed feed = new IndexFeed();
        feed.setForms(new ArrayList<>());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringUtils.readByLine(reader, (line, index) -> parseLine(feed, line, index));
            return feed;
        }
    }

    private static void parseLine(IndexFeed feed, String entry, int index) {
        try {
            if (index <= META_INFO_MAX_LINE) {
                updateMeta(feed, entry, index);
            } else if (index >= FORM_START_LINE) {
                updateForms(feed, entry);
            }
        } catch (ParseException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid feed entry at line " + index + ": " + entry);
        }
    }

    private static void updateForms(IndexFeed feed, String line) throws ParseException {
        String[] parts = line.split("\\|");

        if (parts.length != 5)
            throw new ParseException("Invalid number of data parts", 0);

        // Accession number
        int accNoIdx = parts[4].lastIndexOf("/") + 1;
        String accNo = parts[4].substring(accNoIdx, parts[4].length() - 4);

        // Issuer details
        String issueCik = parts[0];
        String issueName = parts[1];

        // Form type & Filing date
        FormType type = FormType.ofValue(parts[2]);
        Date filedAt = DateUtils.parse(parts[3], "yyyy-MM-dd");

        IndexForm form = new IndexForm(accNo, issueCik, issueName, type, filedAt);
        feed.getForms().add(form);
    }

    private static void updateMeta(IndexFeed feed, String entry, int index) throws ParseException {
        String value = entry.substring(entry.indexOf(":") + 1).trim();
        switch (index) {
            case 0:
                feed.setDescription(value);
                return;
            case 1:
                Date updatedAt = DateUtils.parse(value, "MMMM d, yyyy");
                feed.setUpdatedAt(updatedAt);
                return;
            case 2:
                feed.setComments(value);
                return;
            case 3:
                feed.setAnonymousFTP(value);
                return;
            case 4:
                feed.setCloudHTTP(value);
        }
    }

}
