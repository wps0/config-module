package pl.wieczorekp.configmodule.tests;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.Language;

import static org.junit.Assert.*;

public class LanguageTest {
    private Language language;
    @Before
    public void setup() {
        language = Language.ENGLISH;
    }

    @Test
    public void getId() {
        assertEquals(language.getId(), "en");
    }

    @Test
    public void getPath() {
        assertEquals(language.getPath(), "en.");
    }

    @Test
    public void fromStringCode_correct() {
        assertEquals(Language.fromStringCode("en"), Language.ENGLISH);
    }

    @Test
    public void fromStringCode_incorrect() {
        assertNull(Language.fromStringCode("Gw13zdny\npAtrol"));
    }
}
