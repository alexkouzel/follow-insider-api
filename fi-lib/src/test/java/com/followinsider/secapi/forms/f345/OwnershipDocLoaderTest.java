package com.followinsider.secapi.forms.f345;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRef;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OwnershipDocLoaderTest {

    private static final OwnershipDocLoader loader = new OwnershipDocLoader();

    @Test
    public void loadByRef() throws IOException, ParseException {
        FormRef ref = new FormRef("0001127602-23-028345", "1000753", FormType.F4, null);
        OwnershipDoc doc = loader.loadByRef(ref);
        verifyOwnershipDoc(doc);
    }

    private void verifyOwnershipDoc(OwnershipDoc doc) {
        assertNotNull(doc);

        // TODO: Implement this.
    }

}
