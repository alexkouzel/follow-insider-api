package com.followinsider.core.forms.f345;

import com.followinsider.forms.f345.OwnershipDoc;
import com.followinsider.forms.f345.OwnershipDocLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OwnershipDocLoaderTest {

    private static OwnershipDocLoader loader = new OwnershipDocLoader();

    @Test
    public void loadByUrl() throws IOException, ParseException {
        String url = "https://www.sec.gov/Archives/edgar/data/1000753/000112760223028345/0001127602-23-028345.txt";
        OwnershipDoc doc = loader.loadByUrl(url);
        verifyOwnershipDoc(doc);
    }

    private void verifyOwnershipDoc(OwnershipDoc doc) {
        assertNotNull(doc);

        // TODO: Implement this.
    }

}
