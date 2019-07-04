package pl.wieczorekp.configmodule;

import lombok.Getter;

import java.io.File;

/**
 * ToDo: dynamicznie dodawanie do entries.
 */
public class ConfigFile extends File {
    @Getter private ConfigEntryHashMap entries;

    public ConfigFile(String path, ConfigEntryHashMap entries) {
        super(path);
        this.entries = entries;
    }
}
