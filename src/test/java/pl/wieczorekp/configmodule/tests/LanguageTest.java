package pl.wieczorekp.configmodule.tests;

import org.junit.Test;
import pl.wieczorekp.configmodule.Language;

import static org.junit.Assert.*;

public class LanguageTest {
    @Test
    public void getId() {
        Language language = Language.ENGLISH;
        assertEquals(language.getId(), "en");
    }

    @Test
    public void getPath() {
        Language language = Language.ENGLISH;
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