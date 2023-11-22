package com.followinsider.secapi.forms.f345;

import com.followinsider.secapi.TestCase;
import com.followinsider.secapi.forms.f345.transaction.derivative.DerivativeTable;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OwnershipFormParserTest extends TestCase {

    @Test
    public void parseTeslaForm4() throws IOException, ParseException {
        OwnershipForm actual = loadXmlResource("secapi/tesla-form4.xml", OwnershipForm.class);
        OwnershipForm expected = OwnershipFormFactory.build();
        assertDocumentEquals(expected, actual);
    }

    private void assertDocumentEquals(OwnershipForm expected, OwnershipForm actual) {

        // Test derivative table
        DerivativeTable d1 = expected.getDerivativeTable();
        DerivativeTable d2 = actual.getDerivativeTable();
        partialAssert(d1.getDerivativeHolding(), d2.getDerivativeHolding());
        partialAssert(d1.getDerivativeTransaction(), d2.getDerivativeTransaction());

        // Test non-derivative table
        NonDerivativeTable n1 = expected.getNonDerivativeTable();
        NonDerivativeTable n2 = actual.getNonDerivativeTable();
        partialAssert(n1.getNonDerivativeHolding(), n2.getNonDerivativeHolding());
        partialAssert(n1.getNonDerivativeTransaction(), n2.getNonDerivativeTransaction());

        // Test everything else
        expected.setNonDerivativeTable(null);
        expected.setDerivativeTable(null);
        actual.setNonDerivativeTable(null);
        actual.setDerivativeTable(null);
        assertEquals(expected, actual);
    }

    private <T> void partialAssert(List<T> expected, List<T> actual) {
        if (expected == null) return;
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

}
