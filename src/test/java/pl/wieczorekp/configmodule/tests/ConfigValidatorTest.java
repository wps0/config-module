package pl.wieczorekp.configmodule.tests;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.*;

import java.io.File;
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

//    @Test
//    public void revertOriginal() throws IOException {
//        File file = new File(testPlugin.getDataFolder(), "config");
//        file.createNewFile();
//        file.deleteOnExit();
//        ConfigValidator.revertOriginal(testPlugin.getDataFolder().getAbsolutePath() + separator + "config", testPlugin);
//
//        boolean oldf = false, newf = false;
//        for (File f : testPlugin.getDataFolder().listFiles()) {
//            if (f.getName().equals("config.old"))
//                oldf = true;
//            else if (f.getName().equals("config"))
//                newf = true;
//
//            if (oldf && newf)
//                break;
//        }
//
//        assertTrue(oldf && newf);
//    }

    @Test
    public void validateEntry() {
        fail("This test has yet to be implemented.");
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
        fail("This test has yet to be implemented.");
    }

    @Test
    public void revertOriginal1() {
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
