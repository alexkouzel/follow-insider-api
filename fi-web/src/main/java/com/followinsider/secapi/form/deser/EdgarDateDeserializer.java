package com.followinsider.secapi.form.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.followinsider.util.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class EdgarDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String dateValue = parser.getValueAsString();
        if (dateValue.length() > 10) {
            // Substring to avoid values such as "2023-08-16-05:00"
            dateValue = dateValue.substring(0, 10);
        }
        try {
            return DateUtils.parse(dateValue, "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new IOException("Invalid date: " + dateValue);
        }
    }
    
}
