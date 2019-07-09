package pl.wieczorekp.configmodule;

import lombok.Getter;

import java.io.File;

/**
 * ToDo: dynamicznie dodawanie do entries.
 */
public class ConfigFile extends File {
    /**
     * The minimum delay between file reloads.
     */
    private static final int reloadTolerance = 1000;
    private long lastReload;
    @Getter private ConfigEntryHashMap entries;

    public ConfigFile(File parent, String path, ConfigEntryHashMap entries) {
        super(parent, path);
        this.entries = entries;
        this.lastReload = -1;
    }

    public boolean canReload() {
        return System.currentTimeMillis() - lastReload > reloadTolerance;
    }

    public void updateLastReload() {
        updateLastReload(System.currentTimeMillis());
    }

    public void updateLastReload(long lastReload) {
        this.lastReload = lastReload;
    }
}
