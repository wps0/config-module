package pl.wieczorekp.configmodule.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.Config;
import pl.wieczorekp.configmodule.ConfigEntry;
import pl.wieczorekp.configmodule.ConfigEntryList;
import pl.wieczorekp.configmodule.ConfigValidator;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConfigValidatorTest {
    private ConfigValidator validator;

    @Before
    public void setUp() throws Exception {
        File dataFolder = new File(System.getProperty("user.dir"));
        validator = new ConfigValidator(null, dataFolder, "Prefix", new ConfigEntryList(
                ConfigEntryList.makeList( /*int*/
                        new ConfigEntry<>("int1", "config.yml/superpath"),
                        new ConfigEntry<>("int2", "config.yml"),
                        new ConfigEntry<>("int3", "config.yml/testing.multilevel.arrays")
                ), ConfigEntryList.makeList( /*bool*/
                        new ConfigEntry<>("bool1", "config.yml"),
                        new ConfigEntry<>("bool2", "ala/config.yml"),
                        new ConfigEntry<>("bool3", "ala/config.yml/super")
                ), ConfigEntryList.makeList( /*string*/
                        new ConfigEntry<>("string1", "config.yml"),
                        new ConfigEntry<>("int2", "config.yml/stringtest"),
                        new ConfigEntry<>("int3", "string/test.yml")
                ),ConfigEntryList.makeList( /*object*/
                        new ConfigEntry<>("int1", "config.yml/superpath"),
                        new ConfigEntry<>("int2", "config.yml"),
                        new ConfigEntry<>("int3", "config.yml/ala.ma.kota")
                )
        )) {
            @Override
            protected boolean additionalBeforeValidation() {
                return true;
            }

            @Override
            protected boolean additionalAfterValidation() {
                return true;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void validate() {
    }

    @Test
    public void revertOriginal() {
    }

    @Test
    public void validateEntry() {
    }

    @Test
    public void validateEntry1() {
    }

    @Test
    public void addPath() {
    }

    @Test
    public void printError() {
    }

    @Test
    public void getFilePathFromConfig() {
    }

    @Test
    public void createFileFromPath() {
    }
}
