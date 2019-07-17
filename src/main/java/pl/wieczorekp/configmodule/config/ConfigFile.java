package pl.wieczorekp.configmodule.config;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
