package pl.wieczorekp.configmodule.tests;

import org.junit.Test;
import pl.wieczorekp.configmodule.ConfigEntryHashMap;
import pl.wieczorekp.configmodule.ConfigFile;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigFileTest {

    @Test
    public void getEntries() {
        ConfigEntryHashMap configEntryHashMap = new ConfigEntryHashMap();
        ConfigFile configFile = new ConfigFile(new File("."), "config/config.yml", configEntryHashMap);
        assertEquals("should return the same object which used in constructor", configEntryHashMap, configFile.getEntries());
    }
}
