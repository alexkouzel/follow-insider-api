package com.followinsider.secapi.forms.f345;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.followinsider.secapi.forms.FormUtils;
import com.followinsider.utils.DateUtils;
import com.followinsider.utils.StringUtils;

import java.text.ParseException;
import java.util.Date;

public class OwnershipDocumentParser {

    public static OwnershipDocument parseText(String text) throws ParseException, JsonProcessingException {
        OwnershipDocument doc = new OwnershipDocument();
        parseSecHeader(doc, text);
        parseXmlForm(doc, text);
        parseUrls(doc, text);
        return doc;
    }

    private static void parseSecHeader(OwnershipDocument doc, String data) throws ParseException {
        String header = StringUtils.substring(data, "<SEC-HEADER>", "</SEC-HEADER>");

        if (header != null) {
            String metadata = header.substring(0, header.indexOf("\n\n"));
            String[] fields = metadata.split("\n");

            doc.setAccNo(parseStrField(fields[2]));
            doc.setSubmissionType(parseStrField(fields[3]));
            doc.setDocCount(parseIntField(fields[4]));
            doc.setReportedAt(parseDateField(fields[5]));
            doc.setFiledAt(parseDateField(fields[6]));
            doc.setUpdatedAt(parseDateField(fields[7]));
        }
    }

    private static void parseXmlForm(OwnershipDocument doc, String data) throws JsonProcessingException {
        String xml = StringUtils.substring(data, "<XML>", "</XML>");
        if (xml != null) {
            xml = xml.trim();
            if (xml.startsWith("<xml>")) {
                xml = xml.substring(xml.indexOf("\n") + 1);
            }
            OwnershipForm form = new XmlMapper().readValue(xml, OwnershipForm.class);
            doc.setOwnershipForm(form);
        }
    }

    private static void parseUrls(OwnershipDocument doc, String data) {
        String issuerCik = doc.getOwnershipForm().getIssuer().getIssuerCik();
        String shortIssueCik = StringUtils.trimLeading(issuerCik, '0');
        String xmlFilename = StringUtils.substring(data, "<FILENAME>", "\n");
        String xmlUrl = FormUtils.getXmlUrl(shortIssueCik, doc.getAccNo(), xmlFilename);
        String txtUrl = FormUtils.getTxtUrl(shortIssueCik, doc.getAccNo());
        doc.setTxtUrl(txtUrl);
        doc.setXmlUrl(xmlUrl);
    }

    private static Integer parseIntField(String field) {
        return Integer.parseInt(parseStrField(field));
    }

    private static Date parseDateField(String field) throws ParseException {
        return DateUtils.parse(parseStrField(field), "yyyyMMdd");
    }

    private static String parseStrField(String field) {
        return field.substring(field.lastIndexOf("\t") + 1);
    }

}
