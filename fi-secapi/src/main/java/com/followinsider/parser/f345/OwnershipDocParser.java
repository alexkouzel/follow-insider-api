package com.followinsider.parser.f345;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.followinsider.util.FormUtil;
import com.followinsider.util.DateUtil;
import com.followinsider.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

public class OwnershipDocParser {

    public static OwnershipDoc parse(String text) throws ParseException {
        OwnershipDoc doc = new OwnershipDoc();
        parseSecHeader(doc, text);
        parseXmlForm(doc, text);
        parseUrls(doc, text);
        return doc;
    }

    private static void parseSecHeader(OwnershipDoc doc, String data) throws ParseException {
        String header = StringUtil.substring(data, "<SEC-HEADER>", "</SEC-HEADER>");

        if (header != null) {
            String metadata = header.substring(0, header.indexOf("\n\n"));
            String[] fields = metadata.split("\n");

            doc.setAccNum(parseStrField(fields[2]));
            doc.setSubmissionType(parseStrField(fields[3]));
            doc.setDocCount(parseIntField(fields[4]));
            doc.setReportedAt(parseDateField(fields[5]));
            doc.setFiledAt(parseDateField(fields[6]));
            doc.setUpdatedAt(parseDateField(fields[7]));
        }
    }

    private static void parseXmlForm(OwnershipDoc doc, String data) throws ParseException {
        String xml = StringUtil.substring(data, "<XML>", "</XML>");
        if (xml == null) {
            throw new ParseException("<XML> is missing", -1);
        }
        xml = xml.trim();
        if (xml.startsWith("<xml>")) {
            xml = xml.substring(xml.indexOf("\n") + 1);
        }
        try {
            OwnershipForm form = new XmlMapper().readValue(xml, OwnershipForm.class);
            doc.setOwnershipForm(form);
        } catch (JsonProcessingException e) {
            throw new ParseException("Failed to map XML onto OwnershipForm.class", -1);
        }
    }

    private static void parseUrls(OwnershipDoc doc, String data) {
        String issuerCik = doc.getOwnershipForm().getIssuer().getIssuerCik();
        String shortIssueCik = StringUtil.trimLeading(issuerCik, '0');
        String xmlFilename = StringUtil.substring(data, "<FILENAME>", "\n");
        String xmlUrl = FormUtil.getXmlUrl(shortIssueCik, doc.getAccNum(), xmlFilename);
        String txtUrl = FormUtil.getTxtUrl(shortIssueCik, doc.getAccNum());
        doc.setTxtUrl(txtUrl);
        doc.setXmlUrl(xmlUrl);
    }

    private static Integer parseIntField(String field) {
        return Integer.parseInt(parseStrField(field));
    }

    private static Date parseDateField(String field) throws ParseException {
        return DateUtil.parse(parseStrField(field), "yyyyMMdd");
    }

    private static String parseStrField(String field) {
        return field.substring(field.lastIndexOf("\t") + 1);
    }

}
