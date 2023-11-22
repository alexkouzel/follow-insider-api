package com.followinsider.secapi.forms.refs.daily;

import com.followinsider.secapi.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DailyFeedParserTest extends TestCase {

    @Test
    public void parseDailyFeed() throws IOException, ParseException {
        String txt = loadTxtResource("secapi/daily-feed.txt");
        DailyFeed actual = DailyFeedParser.parseFeed(txt);
        DailyFeed expected = DailyFeedFactory.build();
        assertFeedEquals(expected, actual);
    }

    private void assertFeedEquals(DailyFeed expected, DailyFeed actual) {
        for (int i = 0; i < expected.getForms().size(); i++) {
            assertEquals(expected.getForms().get(i), actual.getForms().get(i));
        }
        expected.setForms(null);
        actual.setForms(null);
        assertEquals(expected, actual);
    }

}
