package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.TestCase;
import com.followinsider.secapi.forms.f345.OwnershipDocument;
import com.followinsider.secapi.forms.f345.OwnershipDocumentParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InsiderFormParserTest extends TestCase {

    @Test
    public void parseOwnershipDoc() throws IOException, ParseException {
        String data = loadTxtResource("secapi/ownership-doc.txt");
        OwnershipDocument doc = OwnershipDocumentParser.parseText(data);
        InsiderForm actual = InsiderFormParser.parseOwnershipDocument(doc);
        InsiderForm expected = InsiderFormFactory.build();
        assertFormEquals(expected, actual);
    }

    private void assertFormEquals(InsiderForm expected, InsiderForm actual) {
        expected.getTrades().sort(Comparator.comparing(InsiderTrade::getExecutedAt));
        actual.getTrades().sort(Comparator.comparing(InsiderTrade::getExecutedAt));
        for (int i = 0; i < expected.getTrades().size(); i++) {
            assertEquals(expected.getTrades().get(i), actual.getTrades().get(i));
        }
        actual.setTrades(null);
        expected.setTrades(null);
        assertEquals(expected, actual);
    }

}
