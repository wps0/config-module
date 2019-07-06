package pl.wieczorekp.configmodule.tests;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.ConfigEntry;
import pl.wieczorekp.configmodule.Language;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigEntryTest {
    private ConfigEntry<Integer> configEntry;

    @Before
    public void setUp() {
        configEntry = new ConfigEntry<>("randomInt");
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
        ConfigEntry<Language> languageConfigEntry = new ConfigEntry<>("lang");
        languageConfigEntry.setValue(Language.ENGLISH);
        assertTrue("ConfigEntry should be language", languageConfigEntry.is(Language.class));
        assertFalse("ConfigEntry should be language, but is string", languageConfigEntry.is(String.class));
        assertFalse("ConfigEntry should be language, but is integer", languageConfigEntry.is(Integer.class));
        assertFalse("ConfigEntry should be language, but is boolean", languageConfigEntry.is(Boolean.class));
    }

    @Test
    public void is_boolean() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry");
        booleanConfigEntry.setValue(false);
        assertTrue("ConfigEntry should be boolean", booleanConfigEntry.is(Boolean.class));
        assertFalse("ConfigEntry should be boolean, but is string", booleanConfigEntry.is(String.class));
        assertFalse("ConfigEntry should be boolean, but is integer", booleanConfigEntry.is(Integer.class));
        assertFalse("ConfigEntry should be boolean, but is language", booleanConfigEntry.is(Language.class));
    }

    @Test
    public void is_integer() {
        configEntry.setValue(1023218730);
        assertTrue("ConfigEntry should be integer", configEntry.is(Integer.class));
        assertFalse("ConfigEntry should be integer, but is string", configEntry.is(String.class));
        assertFalse("ConfigEntry should be integer, but is boolean", configEntry.is(Boolean.class));
        assertFalse("ConfigEntry should be integer, but is language", configEntry.is(Language.class));
    }

    @Test
    public void is_string() {
        ConfigEntry<String> stringConfigEntry = new ConfigEntry<>("stringEntry", "aa");
        assertTrue("ConfigEntry should be string", stringConfigEntry.is(String.class));
        assertFalse("ConfigEntry should be string, but is integer", stringConfigEntry.is(Integer.class));
        assertFalse("ConfigEntry should be string, but is boolean", stringConfigEntry.is(Boolean.class));
        assertFalse("ConfigEntry should be string, but is language", stringConfigEntry.is(Language.class));
    }

    @Test()
    public void is_null() {
        assertFalse(configEntry.is(Integer.class));
    }

    @Test
    public void getPathInFile_basic() {
        assertEquals("randomInt", configEntry.getName());
    }
    @Test
    public void getPathInFile_notNull() {
        configEntry = new ConfigEntry<>("AlaMaKota", 2);
        assertEquals("AlaMaKota", configEntry.getName());
    }
    @Test
    public void getPathInFile_multi() {
        configEntry = new ConfigEntry<>("super.secret.list.string3");
        assertEquals("super.secret.list.string3", configEntry.getName());
    }

    @Test
    public void validate_integer() {
        YamlConfiguration yml = mock(YamlConfiguration.class);
        configEntry.setValue(459539);

        when(yml.isInt(configEntry.getName())).thenReturn(true);
        assertTrue("should be able to validate integer", configEntry.validate(yml));
    }

    @Test
    public void validate_language() {
        YamlConfiguration yml = mock(YamlConfiguration.class);
        ConfigEntry<Language> langCE = new ConfigEntry<>("language", Language.ENGLISH);

        when(yml.isString(langCE.getValue().getId() + "." + langCE.getName())).thenReturn(true);
        when(yml.isString(Language.POLISH.getId()  + "." + langCE.getName())).thenReturn(true);

        assertTrue("should be able to validate language", langCE.validate(yml));
    }
    @Test
    public void validate_falseLanguage() {
        YamlConfiguration yml = mock(YamlConfiguration.class);
        ConfigEntry<Language> langCE = new ConfigEntry<>("language", Language.ENGLISH);

        when(yml.isString(langCE.getValue().getId() + "." + langCE.getName())).thenReturn(true);
        when(yml.isString(Language.POLISH.getId()  + "." + langCE.getName())).thenReturn(false);

        assertFalse("should be able to validate incorrect language", langCE.validate(yml));
    }

    @Test
    public void validate_boolean() {
        YamlConfiguration yml = mock(YamlConfiguration.class);
        ConfigEntry<Boolean> booleanCE = new ConfigEntry<>("bool", true);

        when(yml.isBoolean(booleanCE.getName())).thenReturn(true);

        assertTrue("should be able to validate booleans", booleanCE.validate(yml));
    }

    @Test
    public void validate_string() {
        YamlConfiguration yml = mock(YamlConfiguration.class);
        ConfigEntry<String> stringCE = new ConfigEntry<>("string", "ala");

        when(yml.isBoolean(stringCE.getName())).thenReturn(true);

        assertTrue("should be able to validate booleans", stringCE.validate(yml));
    }
}
