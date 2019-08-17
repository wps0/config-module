package pl.wieczorekp.configmodule;

import org.bukkit.configuration.file.FileConfiguration;
import pl.wieczorekp.configmodule.config.Config;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface IConfigurableJavaPlugin {
    IConfigurableJavaPlugin getInstance();
    Config getConfigInstance();

    ///////////////////////////////////////////////////////////////////////////
    // Override some of the JavaPlugin methods
    ///////////////////////////////////////////////////////////////////////////
    String getName();
    File getDataFolder();
    FileConfiguration getConfig();
    InputStream getResource(String filename);
    Logger getLogger();
    void saveResource(String resourcePath, boolean replace);
}
