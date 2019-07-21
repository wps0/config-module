package pl.wieczorekp.configmodule.tests.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.*;
import pl.wieczorekp.configmodule.config.Config;
import pl.wieczorekp.configmodule.config.ConfigEntry;
import pl.wieczorekp.configmodule.config.ConfigFile;
import pl.wieczorekp.configmodule.config.ConfigValidator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static java.io.File.separatorChar;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConfigValidatorTest {

    @Before
    public void setUp() {
//        validator = new ConfigValidator(testPlugin, testPlugin.getDataFolder(), "Prefix", new ConfigEntryHashMap(
//                ConfigEntryList.makeList( /*int*/
//                        new ConfigEntry<>("int1", "config.yml/superpath"),
//                        new ConfigEntry<>("int2", "config.yml"),
//                        new ConfigEntry<>("int3", "config.yml/testing.multilevel.arrays")
//                ), ConfigEntryList.makeList( /*bool*/
//                        new ConfigEntry<>("bool1", "config.yml"),
//                        new ConfigEntry<>("bool2", "ala/config.yml"),
//                        new ConfigEntry<>("bool3", "ala/config.yml/super")
//                ), ConfigEntryList.makeList( /*string*/
//                        new ConfigEntry<>("string1", "config.yml"),
//                        new ConfigEntry<>("string2", "config.yml/stringtest"),
//                        new ConfigEntry<>("string3", "oj/string/test.yml/kropka")
//                ),ConfigEntryList.makeList( /*object*/
//                        new ConfigEntry<>("object1", "config.yml/superpath"),
//                        new ConfigEntry<>("object2", "config.yml"),
//                        new ConfigEntry<>("object3", "config.yml/ala.ma.kota")
//                )
//        )) {
//            @Override
//            protected boolean additionalBeforeValidation() {
//                return true;
//            }
//
//            @Override
//            protected boolean additionalAfterValidation() {
//                return true;
//            }
//        };
    }

    @Test
    public void validate() {
        fail("This test has yet to be implemented.");
        //assertTrue(validator.load());
    }

    @Test
    public void validateEntry() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        fail("Test does not work as expected.");
        IConfigurableJavaPlugin mockICJP = mock(IConfigurableJavaPlugin.class);
        when(mockICJP.getName()).thenReturn("ConfigValidatorTest");
        //given(IConfigurableJavaPlugin.getInstance("pl.wieczorekp")).willReturn(mockICJP);

        ConfigFile configFile = mock(ConfigFile.class);
        YamlConfiguration yml = mock(YamlConfiguration.class);
        ConfigValidator cv = new Config("pl.wieczorekp", configFile);
        ConfigEntry<String> configEntry = new ConfigEntry<>("stringTest", "stringValue", () -> ".");

        Method method = cv.getClass().getDeclaredMethod("validateEntry",configFile.getClass(), configEntry.getClass(), yml.getClass(), boolean.class);
        method.invoke(cv, configEntry, configEntry, yml, false);
    }

    @Test
    public void validateEntry1() {
        fail("This test has yet to be implemented.");
    }

    @Test
    public void printError() {
        fail("This test has yet to be implemented.");
    }

    @Test
    public void getFilePathFromConfig_slash() {
        ConfigValidator.setPathPattern(Pattern.compile("ConfigValidatorTest([/\\\\])(.+)"));
        ConfigFile configFile = mock(ConfigFile.class);
        when(configFile.getPath()).thenReturn("ConfigValidatorTest/cfg/config.yml");

        assertEquals("should convert slashed absolute path to the file to the relative path", "cfg/config.yml", ConfigValidator.getFilePathFromConfig(configFile));
    }
    @Test
    public void getFilePathFromConfig_backslash() {
        ConfigValidator.setPathPattern(Pattern.compile("ConfigValidatorTest([/\\\\])(.+)"));
        ConfigFile configFile = mock(ConfigFile.class);
        when(configFile.getPath()).thenReturn("ConfigValidatorTest\\cfg\\config.yml");

        assertEquals("should convert back slashed absolute path to the file to the relative path", "cfg\\config.yml", ConfigValidator.getFilePathFromConfig(configFile));
    }

    @Test
    public void createFileFromPath() {
        fail("This test has yet to be implemented.");
    }

    @Test
    public void load() {
        fail("This test has yet to be implemented.");
    }

    @Test
    public void revertOriginal() {
        fail("Test does not work as expected.");
        String path = System.getProperty("user.dir");
        File old = new File(path + separatorChar + "test.yml.old");
        old.deleteOnExit();

//        ConfigFile configFile = new ConfigFile(new File(path), "config.yml", new ConfigEntryHashMap());
        ConfigFile configFile = mock(ConfigFile.class);
        configFile.deleteOnExit();
        when(configFile.getAbsolutePath()).thenReturn("aa/test.yml");
        when(configFile.renameTo(old)).thenReturn(true);

        IConfigurableJavaPlugin mockICJP = mock(IConfigurableJavaPlugin.class);
        ConfigValidator.revertOriginal(configFile, mockICJP);
    }

    @Test
    public void setPathPattern() {
        assertNull("at the beginning, pathPattern should be null", ConfigValidator.getPathPattern());
        ConfigValidator.setPathPattern(Pattern.compile("([/\\\\])(.+)"));

        assertEquals("pathPattern should be set to previously specified value", "([/\\\\])(.+)", ConfigValidator.getPathPattern().toString());
    }
}
