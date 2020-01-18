package pl.wieczorekp.configmodule.config.filesystem;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import pl.wieczorekp.configmodule.config.ConfigEntryHashMap;

import java.io.File;
import java.util.*;

public class ConfigFile extends File {
    /**
     * The minimum delay between file reloads.
     */
    private static final int reloadTolerance = 1000;
    private long lastReload;
    @Getter private boolean isMessageFile;
    @Getter private ConfigEntryHashMap entries;

    public ConfigFile(File parent, String path, ConfigEntryHashMap entries) {
        super(parent, path);
        this.entries = entries;
        this.lastReload = -1;
        this.isMessageFile = false;
    }
    public ConfigFile(File parent, @NotNull String child, ConfigEntryHashMap entries, boolean isMessageFile) {
        this(parent, child, entries);
        this.isMessageFile = isMessageFile;
    }
    ///////////////////////////////////////////////////////////////////////////
    // Reload-related
    ///////////////////////////////////////////////////////////////////////////
    public boolean canReload() {
        return System.currentTimeMillis() - lastReload > reloadTolerance;
    }
    public void updateLastReload() {
        updateLastReload(System.currentTimeMillis());
    }
    public void updateLastReload(long lastReload) {
        this.lastReload = lastReload;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////
    public YamlConfiguration getYamlConfiguration() {
        return YamlConfiguration.loadConfiguration(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////
    public static ConfigFile[] toArray(Collection<ConfigFile> fileCollection) {
        ConfigFile[] files = new ConfigFile[fileCollection.size()];
        Iterator<ConfigFile> iterator = fileCollection.iterator();

        for (int i = 0; i <  fileCollection.size(); i++) {
            files[i] = iterator.next();
        }

        return files;
    }
    public static ConfigFile[] toArray(Map<Object, ConfigFile> fileMap) {
        ConfigFile[] files = new ConfigFile[fileMap.values().size()];
        Iterator<ConfigFile> iterator = fileMap.values().iterator();

        for (int i = 0; i <  fileMap.values().size(); i++) {
            files[i] = iterator.next();
        }

        return files;
    }
}
