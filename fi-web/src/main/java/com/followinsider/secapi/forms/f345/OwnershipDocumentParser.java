package com.followinsider.secapi.forms.f345;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.followinsider.secapi.forms.FormUtils;
import com.followinsider.secapi.utils.DateUtils;
import com.followinsider.secapi.utils.StringUtils;

import java.text.ParseException;
import java.util.Date;

public class OwnershipDocumentParser {

    public static OwnershipDocument parseText(String text) throws ParseException, JsonProcessingException {
        OwnershipDocument doc = new OwnershipDocument();
        parseHeader(doc, text);
        parseForm(doc, text);
        parseUrls(doc, text);
        return doc;
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

    private static void parseForm(OwnershipDocument doc, String data) throws JsonProcessingException {
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

    private static void parseHeader(OwnershipDocument doc, String data) throws ParseException {
        String header = StringUtils.substring(data, "<SEC-HEADER>", "</SEC-HEADER>");
        if (header != null) parseMetadata(doc, header);
    }

    private static void parseMetadata(OwnershipDocument doc, String header) throws ParseException {
        String metadata = header.substring(0, header.indexOf("\n\n"));
        String[] fields = metadata.split("\n");
        doc.setAccNo(extractStr(fields[2]));
        doc.setSubmissionType(extractStr(fields[3]));
        doc.setDocCount(extractInt(fields[4]));
        doc.setReportedAt(extractDate(fields[5]));
        doc.setFiledAt(extractDate(fields[6]));
        doc.setUpdatedAt(extractDate(fields[7]));
    }

    private static Integer extractInt(String field) {
        return Integer.parseInt(extractStr(field));
    }

    private static Date extractDate(String field) throws ParseException {
        return DateUtils.parse(extractStr(field), "yyyyMMdd");
    }

    private static String extractStr(String field) {
        return field.substring(field.lastIndexOf("\t") + 1);
    }

}
