package com.followinsider.secapi.forms.f345;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.secapi.forms.FormUrlParser;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class OwnershipDocParser {

    private final XmlMapper xmlMapper;

    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n");

    public OwnershipDoc parse(String data, FormRef ref) throws ParseException {
        return new OwnershipDoc(
                ref.accNum(),
                ref.getTxtUrl(),
                getXmlUrl(data, ref),
                ref.filedAt(),
                getXmlForm(data)
        );
    }

    private String getXmlUrl(String data, FormRef ref) {
        String filename = StringUtils.substring(data, "<FILENAME>", "\n");
        return FormUrlParser.getXmlUrl(ref.issuerCik(), ref.accNum(), filename);
    }

    private OwnershipForm getXmlForm(String data) throws ParseException {
        try {
            String xml = getXmlData(data);
            return xmlMapper.readValue(xml, OwnershipForm.class);

        } catch (JsonProcessingException e) {
            throw new ParseException("Failed mapping OwnershipForm: " + e.getMessage(), -1);
        }
    }

    private String getXmlData(String data) throws ParseException {
        String xml = StringUtils.substring(data, "<XML>", "</XML>");
        if (xml == null) throw new ParseException("<XML> is missing", -1);

        xml = xml.trim();
        if (xml.startsWith("<xml>")) {
            xml = StringUtils.removeFirstLine(xml);
        }
        xml = NEW_LINE_PATTERN.matcher(xml).replaceAll("");
        return xml;
    }

}
