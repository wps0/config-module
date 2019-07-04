package pl.wieczorekp.configmodule.tests;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.wieczorekp.configmodule.ConfigEntry;
import pl.wieczorekp.configmodule.Language;

import java.io.File;
import java.io.IOException;

import static java.io.File.separatorChar;
import static org.junit.Assert.*;

public class ConfigEntryTest {
    private static File configFile;
    private ConfigEntry<Integer> configEntry;

    @BeforeClass
    public static void setUpClass() {
        configFile = new File(System.getProperty("user.dir") + separatorChar + "config.yml");
    }

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
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertTrue(stringConfigEntry.is(Language.class));
    }
    @Test
    public void is_language_boolean() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertFalse(stringConfigEntry.is(Boolean.class));
    }
    @Test
    public void is_language_integer() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertFalse(stringConfigEntry.is(Integer.class));
    }
    @Test
    public void is_language_string() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertFalse(stringConfigEntry.is(String.class));
    }

    @Test
    public void is_boolean() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry");
        booleanConfigEntry.setValue(false);
        assertTrue(booleanConfigEntry.is(Boolean.class));
    }
    @Test
    public void is_boolean_string() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry");
        booleanConfigEntry.setValue(false);
        assertFalse(booleanConfigEntry.is(String.class));
    }
    @Test
    public void is_boolean_integer() {
        ConfigEntry<Boolean> booleanConfigEntry = new ConfigEntry<>("boolEntry");
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
        ConfigEntry<String> stringConfigEntry = new ConfigEntry<>("stringEntry", "aa");
        assertTrue(stringConfigEntry.is(String.class));
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
    public void validate() throws IOException {
        configFile.deleteOnExit();
        configFile.createNewFile();

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
        String pathInFile = configEntry.getName();
        int value = 117770;

        yml.set((pathInFile == null ? "" : pathInFile), value);
        yml.save(configFile);
        configEntry.setValue(value);

        assertTrue(configEntry.validate(yml));
    }
}
