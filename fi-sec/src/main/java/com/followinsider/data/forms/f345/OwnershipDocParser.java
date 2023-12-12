package com.followinsider.data.forms.f345;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.followinsider.utils.FormUtils;
import com.followinsider.utils.DateUtils;
import com.followinsider.utils.StringUtils;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.util.Date;

@UtilityClass
public class OwnershipDocParser {

    public OwnershipDoc parse(String text) throws ParseException {
        OwnershipDoc doc = new OwnershipDoc();
        parseSecHeader(doc, text);
        parseXmlForm(doc, text);
        parseUrls(doc, text);
        return doc;
    }

    private void parseSecHeader(OwnershipDoc doc, String data) {
        String header = StringUtils.substring(data, "<SEC-HEADER>", "</SEC-HEADER>");

        if (header != null) {
            String metadata = header.substring(0, header.indexOf("\n\n"));
            String[] fields = metadata.split("\n");

            doc.setAccNum(parseString(fields[2]));
            doc.setSubmissionType(parseString(fields[3]));
            doc.setDocCount(parseInt(fields[4]));
            doc.setReportedAt(parseDate(fields[5]));
            doc.setFiledAt(parseDate(fields[6]));
            doc.setUpdatedAt(parseDate(fields[7])); // optional
        }
    }

    private void parseXmlForm(OwnershipDoc doc, String data) throws ParseException {
        String xml = StringUtils.substring(data, "<XML>", "</XML>");
        if (xml == null) {
            throw new ParseException("<XML> field is missing", -1);
        }
        xml = xml.trim();
        if (xml.startsWith("<xml>")) {
            xml = xml.substring(xml.indexOf("\n") + 1);
        }
        try {
//            System.out.println(xml);
            OwnershipForm form = new XmlMapper().readValue(xml, OwnershipForm.class);
            doc.setOwnershipForm(form);

        } catch (JsonProcessingException e) {
            throw new ParseException("Failed to map XML onto OwnershipForm.class: " + e.getMessage(), -1);
        }
    }

    private void parseUrls(OwnershipDoc doc, String data) {
        String issuerCik = doc.getOwnershipForm().getIssuer().getIssuerCik();
        String shortIssuerCik = StringUtils.trimLeading(issuerCik, '0');
        String xmlFilename = StringUtils.substring(data, "<FILENAME>", "\n");
        String xmlUrl = FormUtils.getXmlUrl(shortIssuerCik, doc.getAccNum(), xmlFilename);
        String txtUrl = FormUtils.getTxtUrl(shortIssuerCik, doc.getAccNum());
        doc.setTxtUrl(txtUrl);
        doc.setXmlUrl(xmlUrl);
    }

    private Integer parseInt(String field) {
        return Integer.parseInt(parseString(field));
    }

    private Date parseDate(String field) {
        return DateUtils.tryParse(parseString(field), "yyyyMMdd").orElse(null);
    }

    private String parseString(String field) {
        return field.substring(field.lastIndexOf("\t") + 1);
    }

}
