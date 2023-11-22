package com.followinsider.secapi.forms.refs.index;

import com.followinsider.secapi.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexFeedParserTest extends TestCase {

    @Test
    public void parseIndexFeed() throws IOException, ParseException {
        try (InputStream stream = loadStreamResource("secapi/index-feed.idx")) {
            IndexFeed actual = IndexFeedParser.parseFeed(stream);
            IndexFeed expected = IndexFeedFactory.build();
            assertIndexFeedEquals(expected, actual);
        }
    }

    private void assertIndexFeedEquals(IndexFeed expected, IndexFeed actual) {
        for (int i = 0; i < expected.getForms().size(); i++) {
            assertEquals(expected.getForms().get(i), actual.getForms().get(i));
        }
        expected.setForms(null);
        actual.setForms(null);
        assertEquals(expected, actual);
    }

}
