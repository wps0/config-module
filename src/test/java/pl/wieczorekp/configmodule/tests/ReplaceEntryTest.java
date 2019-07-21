package pl.wieczorekp.configmodule.tests;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.ReplaceEntry;

import static org.junit.Assert.assertEquals;

public class ReplaceEntryTest {
    private ReplaceEntry replaceEntry;

    @Before
    public void setUp() {
        replaceEntry = new ReplaceEntry("kluczyk", "wartosc");
    }

    @Test
    public void getKey() {
        assertEquals("expected key should be equals got key", "kluczyk", replaceEntry.getKey());
    }

    @Test
    public void getValue() {
        assertEquals("expected value should be equals gotten value", "wartosc", replaceEntry.getValue());
    }

    @Test
    public void setValue() {
        replaceEntry.setValue("ala ma kota");
        assertEquals("expected value should be equals gotten value", "ala ma kota", replaceEntry.getValue());
    }
}
