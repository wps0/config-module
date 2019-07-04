package pl.wieczorekp.configmodule.tests;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.wieczorekp.configmodule.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.io.File.separator;
import static java.io.File.separatorChar;
import static org.junit.Assert.*;

public class ConfigValidatorTest {
    private ConfigValidator validator;
    private TestPlugin testPlugin;

    @Before
    public void setUp() throws Exception {
        testPlugin = new TestPlugin();
        testPlugin.f.mkdir();
        testPlugin.f.deleteOnExit();
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
    public void revertOriginal() throws IOException {
        File file = new File(testPlugin.getDataFolder(), "config");
        file.createNewFile();
        file.deleteOnExit();
        ConfigValidator.revertOriginal(testPlugin.getDataFolder().getAbsolutePath() + separator + "config", testPlugin);

        boolean oldf = false, newf = false;
        for (File f : testPlugin.getDataFolder().listFiles()) {
            if (f.getName().equals("config.old"))
                oldf = true;
            else if (f.getName().equals("config"))
                newf = true;

            if (oldf && newf)
                break;
        }

        assertTrue(oldf && newf);
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

    private class TestPlugin implements IConfigurableJavaPlugin {
        public File f = new File(System.getProperty("user.dir") + separatorChar + "CVTest");

        @Override
        public String getName() {
            return null;
        }

        @Override
        public File getDataFolder() {
            return f;
        }

        @Override
        public FileConfiguration getConfig() {
            return null;
        }

        @Override
        public InputStream getResource(String filename) {
            return null;
        }

        @Override
        public void saveResource(String resourcePath, boolean replace) {
            System.out.println("Saving resource " + resourcePath + "...");
            try {
                File file = new File(f, separatorChar + resourcePath);
                file.createNewFile();
                file.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Logger getLogger() {
            return null;
        }

        @Override
        public void saveDefaultConfig() {

        }
    }
}
