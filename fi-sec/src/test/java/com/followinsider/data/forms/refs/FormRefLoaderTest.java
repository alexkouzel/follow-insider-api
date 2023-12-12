package com.followinsider.data.forms.refs;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FormRefLoaderTest {

    private static FormRefLoader loader = new FormRefLoader(FormType.F4);

    @Test
    public void loadByCik() {
        List<FormRef> refs = loader.loadByCik("0000946581");
        assertTrue(refs.size() > 0);
        verifyRef(refs.get(0));
    }

    @Test
    public void loadByQuarter() {
        List<FormRef> refs = loader.loadByQuarter(2022, 3);
        assertTrue(refs.size() > 0);
        verifyRef(refs.get(0));
    }

    @Test
    public void loadDaysAgo() {
        List<FormRef> refs = loader.loadDaysAgo(1);
        assertTrue(refs.size() > 0);
        verifyRef(refs.get(0));
    }

    @Test
    public void loadLatest() {
        List<FormRef> refs = loader.loadLatest(0, 200);
        assertTrue(refs.size() > 0);
        verifyRef(refs.get(0));
    }

    private void verifyRef(FormRef ref) {
        assertNotNull(ref);
        assertNotNull(ref.accNum());
        assertNotNull(ref.issuerCik());
        assertNotNull(ref.type());
        assertNotNull(ref.filedAt());

        assertEquals(20, ref.accNum().length());
        assertEquals(10, ref.issuerCik().length());
    }

}
