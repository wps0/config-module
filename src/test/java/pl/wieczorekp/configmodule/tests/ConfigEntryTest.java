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
        assertFalse(stringConfigEntry.is(Boolean.class));
    }
    @Test
    public void is_language_integer() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertFalse(stringConfigEntry.is(Integer.class));
    }
    @Test
    public void is_language_string() {
        ConfigEntry<Language> stringConfigEntry = new ConfigEntry<>("stringEntry", "o lol ale padaka");
        stringConfigEntry.setValue(Language.ENGLISH);
        assertFalse(stringConfigEntry.is(String.class));
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
    public void getPathInFile_basic() {
        assertEquals("randomInt", ConfigEntry.getPathInFile(configEntry));
    }
    @Test
    public void getPathInFile_notNull() {
        configEntry = new ConfigEntry<>("AlaMaKota", "config.yml/ala.ma.kota");
        assertEquals("ala.ma.kota.AlaMaKota", ConfigEntry.getPathInFile(configEntry));
    }
    @Test
    public void getPathInFile_multilevel() {
        configEntry = new ConfigEntry<>("boolean1", "configuration/config.yml");
        assertEquals("boolean1", ConfigEntry.getPathInFile(configEntry));
    }
    @Test
    public void getPathInFile_multi() {
        configEntry = new ConfigEntry<>("string3", "very/long/directory/tree/configuration/secret.yml/super.secret.list");
        assertEquals("super.secret.list.string3", ConfigEntry.getPathInFile(configEntry));
    }

    @Test
    public void validate() throws IOException {
        configFile.deleteOnExit();
        configFile.createNewFile();

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
        String pathInFile = ConfigEntry.getPathInFile(configEntry);
        int value = 117770;

        yml.set((pathInFile == null ? "" : pathInFile), value);
        yml.save(configFile);
        configEntry.setValue(value);

        assertTrue(configEntry.validate(yml));
    }
}
