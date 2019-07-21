package pl.wieczorekp.configmodule.tests.config;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.config.ConfigEntryHashMap;
import pl.wieczorekp.configmodule.config.ConfigFile;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigFileTest {
    private ConfigFile configFile;

    @Before
    public void setup() {
        configFile = new ConfigFile(new File("."), "config/config.yml", new ConfigEntryHashMap());
    }

    @Test
    public void getEntries() {
        ConfigEntryHashMap configEntryHashMap = new ConfigEntryHashMap();
        ConfigFile configFile = new ConfigFile(new File("."), "config/config.yml", configEntryHashMap);
        assertEquals("should return the same object which used in constructor", configEntryHashMap, configFile.getEntries());
    }

    @Test
    public void canReload_false() {
        configFile.updateLastReload();
        assertFalse("should not be able to reload after configFile called", configFile.canReload());
    }

    @Test
    public void canReload_true() throws InterruptedException {
        configFile.updateLastReload();
        Thread.sleep(1250);
        assertTrue("should be able to reload after passed delay", configFile.canReload());
    }
}
