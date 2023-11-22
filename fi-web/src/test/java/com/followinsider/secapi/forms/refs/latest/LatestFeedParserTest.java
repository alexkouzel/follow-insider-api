package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LatestFeedParserTest extends TestCase {

    @Test
    public void parseLatestFeed() throws IOException, ParseException {
        LatestFeed actual = loadXmlResource("secapi/latest-feed.xml", LatestFeed.class);
        LatestFeed expected = LatestFeedFactory.build();
        assertFeedEquals(expected, actual);
    }

    private void assertFeedEquals(LatestFeed expected, LatestFeed actual) {
        // Test the "entry" field with a limit to the number of entries
        for (int i = 0; i < expected.getEntry().size(); i++) {
            LatestFeedEntry actualEntry = actual.getEntry().get(i);
            actualEntry.setSummary(actualEntry.getSummary().trim());
            assertEquals(actualEntry, actual.getEntry().get(i));
        }
        // Test everything else
        expected.setEntry(null);
        actual.setEntry(null);
        assertEquals(expected, actual);
    }

}