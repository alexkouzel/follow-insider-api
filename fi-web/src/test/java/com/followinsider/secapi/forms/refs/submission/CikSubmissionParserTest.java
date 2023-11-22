package com.followinsider.secapi.forms.refs.submission;

import com.followinsider.secapi.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CikSubmissionParserTest extends TestCase {

    @Test
    public void parseAppleSubmission() throws IOException, ParseException {
        CikSubmission actual = loadJsonResource("secapi/apple-submission.json", CikSubmission.class);
        CikSubmission expected = CikSubmissionFactory.build();
        assertSubmissionsEquals(expected, actual);
    }

    private void assertSubmissionsEquals(CikSubmission expected, CikSubmission actual) {
        // Test the "recent" field with a limit to the number of filings
        List<CikForm> expectedFilings = expected.getFilings().getRecent().extractForms();
        List<CikForm> actualFilings = actual.getFilings().getRecent().extractForms();
        for (int i = 0; i < expectedFilings.size(); i++) {
            assertEquals(expectedFilings.get(i), actualFilings.get(i));
        }
        // Test everything else
        expected.getFilings().setRecent(null);
        actual.getFilings().setRecent(null);
        assertEquals(expected, actual);
    }

}