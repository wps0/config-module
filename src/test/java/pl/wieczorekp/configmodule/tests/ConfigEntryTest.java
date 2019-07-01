package pl.wieczorekp.configmodule.tests;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.ConfigEntry;
import pl.wieczorekp.configmodule.Language;

import static org.junit.Assert.*;

public class ConfigEntryTest {
    private ConfigEntry<Integer> configEntry;

    @Before
    public void setUp() {
        configEntry = new ConfigEntry<>("randomInt", "config.yml");
    }

    @Test
    public void getPath() {
        assertEquals("config.yml", configEntry.getPath());
    }

    @Test
    public void setValue() {
        configEntry.setValue(333);
        assertEquals(333, configEntry.getValue().intValue());
    }

    @Test
    public void getValue_null() {
        assertNull(configEntry.getValue());
    }

    @Test
    public void getValue_notNull() {
        configEntry.setValue(100000);
        assertEquals(100000, configEntry.getValue().intValue());
    }

    @Test
    public void getName() {
        assertEquals("randomInt", configEntry.getName());
    }

    @Test
    public void is_language() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertTrue(stringConfigEntry.is(Language.class));
    }
    @Test
    public void is_language_boolean() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertTrue(stringConfigEntry.is(Boolean.class));
    }
    @Test
    public void is_language_integer() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertTrue(stringConfigEntry.is(Integer.class));
    }
    @Test
    public void is_language_string() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertTrue(stringConfigEntry.is(String.class));
    }

    @Test
    public void is_boolean() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry", "config.yml/boolTest.bool");
        booleanConfigEntry.setValue(false);
        assertTrue(booleanConfigEntry.is(Boolean.class));
    }
    @Test
    public void is_boolean_string() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry", "config.yml/boolTest.bool");
        booleanConfigEntry.setValue(false);
        assertFalse(booleanConfigEntry.is(String.class));
    }
    @Test
    public void is_boolean_integer() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry", "config.yml/boolTest.bool");
        booleanConfigEntry.setValue(false);
        assertFalse(booleanConfigEntry.is(Integer.class));
    }

    @Test
    public void is_integer() {
        configEntry.setValue(1023218730);
        assertTrue(configEntry.is(Integer.class));
    }
    @Test
    public void is_integer_string() {
        configEntry.setValue(32222222);
        assertFalse(configEntry.is(String.class));
    }

    @Test
    public void is_string() {
        ConfigEntry<String> stringConfigEntry = new ConfigEntry<>("stringEntry", "config.yml/asds.a", "aa", null);
        assertTrue(stringConfigEntry.is(String.class));
    }

    @Test
    public void is_null() {
        assertFalse(configEntry.is(Integer.class));
    }

    @Test
    public void getPathInFile_null() {
        assertNull(ConfigEntry.getPathInFile(configEntry.getPath()));
    }
    @Test
    public void getPathInFile_notNull() {
        configEntry = new ConfigEntry<>("AlaMaKota", "config.yml/ala.ma.kota");
        assertNotNull(ConfigEntry.getPathInFile(configEntry.getPath()));
    }

}
