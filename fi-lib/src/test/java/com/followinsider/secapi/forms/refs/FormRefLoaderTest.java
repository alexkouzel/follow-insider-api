package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.forms.FormType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FormRefLoaderTest {

    private static final FormRefLoader loader = new FormRefLoader(FormType.F4);

    @Test
    public void loadByCik() {
        List<FormRef> refs = loader.loadByCik("0000946581");
        assertFalse(refs.isEmpty());
        verifyRef(refs.get(0));
    }

    @Test
    public void loadByQuarter() {
        List<FormRef> refs = loader.loadByQuarter(2022, 3);
        assertFalse(refs.isEmpty());
        verifyRef(refs.get(0));
    }

    @Test
    public void loadDaysAgo() {
        List<FormRef> refs = loader.loadDaysAgo(1);
        assertFalse(refs.isEmpty());
        verifyRef(refs.get(0));
    }

    @Test
    public void loadLatest() {
        List<FormRef> refs = loader.loadLatest(0, 200);
        assertFalse(refs.isEmpty());
        verifyRef(refs.get(0));
    }

    private void verifyRef(FormRef ref) {
        assertNotNull(ref);
        assertNotNull(ref.accNum());
        assertNotNull(ref.issuerCik());
        assertNotNull(ref.type());
        assertNotNull(ref.filedAt());

        assertEquals(20, ref.accNum().length());
        assertTrue(ref.issuerCik().length() <= 10);
    }

}
