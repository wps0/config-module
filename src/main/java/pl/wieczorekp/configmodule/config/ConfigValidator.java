package pl.wieczorekp.configmodule.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import pl.wieczorekp.configmodule.IConfigurableJavaPlugin;
import pl.wieczorekp.configmodule.utils.ConfigUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public abstract class ConfigValidator {
    protected final IConfigurableJavaPlugin plugin;

    protected final String prefix;
    protected final Logger logger;
    protected Set<ConfigFile> messageFiles;
    protected HashMap<String, @NotNull ConfigFile> configFiles;

    /*
     * ToDo: chyba że zamiast tego ConfigFiles walić pobierać typy i ogólnie wszystki z folderu głównego tego pluginu
     *  i wtedy do konstruktora walnąć, żeby otrzymywał stringa z zoot paczką, w której są config pliki
     *  zapomniałem co chciałem napisać, ripek
     *  dzięki działa
     */
    protected ConfigValidator(@NotNull IConfigurableJavaPlugin plugin, String prefix, @NotNull ConfigFile... files) {
        this.plugin = plugin;
        this.prefix = prefix;
        this.configFiles = new HashMap<>();
        this.messageFiles = new HashSet<>();
        this.logger = plugin.getLogger();

        for (ConfigFile cf : files) {
            configFiles.putIfAbsent(cf.getPath(), cf);

            if (cf.isMessageFile()) {
                messageFiles.add(cf);
            }
        }
    }

    protected ConfigValidator(@NotNull IConfigurableJavaPlugin plugin, @NotNull ConfigFile... files) {
        this(plugin, plugin.getName() + " ", files);
    }

    protected void loadValues(ConfigFile[] files, String[] ids, String idPrefix) {

        for (ConfigFile file : files) {
            if (!file.exists()) {
                logger.fine(file.getName() + " does not exists!");

                String path = ConfigUtils.getFilePathFromConfig(file, plugin.getName());
                if (plugin.getResource(path) == null)
                    throw new IllegalArgumentException("file " + path + " does not exists in plugin's jar file");

                plugin.saveResource(path, false);

                if (!file.exists())
                    throw new RuntimeException("could not create file " + file.getPath());
            } else
                logger.fine(file.getName() + " exists!");

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

            for (String id : ids) {
                String configValue = (String) yml.get(idPrefix + id);

                if (configValue != null) {
                    file.getEntries().put(id, new ConfigEntry<>(id, configValue, String::new));
                }
            }
        }
    }

    private void loadValues(ConfigFile[] files) {
        loadValues(files, null, "");
    }

    public synchronized boolean load() throws IOException {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir())
            throw new IOException("could not create directory in plugins/" + plugin.getName());

        if (configFiles == null || configFiles.size() == 0)
            return true;

        loadValues((ConfigFile[]) configFiles.values().toArray());

        return true;
    }

    /**
     * @param configFile file to be reverted
     * @param _rootInstance instance of the {@link IConfigurableJavaPlugin}
     *
     * @throws IOException when one of the file operations failed (delete/rename)
     */
    public static void revertOriginal(@NotNull ConfigFile configFile, IConfigurableJavaPlugin _rootInstance) throws IOException {
        System.out.println("revert");
        File oldFile = new File(configFile.getAbsolutePath()); // ToDo: nie bedzie czasami z .olda bralo?
        File prevOldFile = new File(configFile.getAbsolutePath() + ".old");

        if (prevOldFile.exists() && !prevOldFile.delete())
            throw new IOException("cannot delete file " + prevOldFile.getName());

        if (!oldFile.renameTo(prevOldFile))
            throw new IOException("cannot rename file to " + oldFile.getName());

        _rootInstance.saveResource(ConfigUtils.getFilePathFromConfig(configFile, _rootInstance.getName()), false);
    }

    public void validateFile(@NotNull ConfigFile configFile) {
        YamlConfiguration yml = configFile.getYamlConfiguration();

        validateEntries(configFile, configFile.getEntries().getBooleans().values(), yml);
        validateEntries(configFile, configFile.getEntries().getIntegers().values(), yml);
        validateEntries(configFile, configFile.getEntries().getStrings().values(), yml);
        validateEntries(configFile, configFile.getEntries().getObjects().values(), yml);
    }

    protected <T> void validateEntries(@NotNull ConfigFile configFile, @NotNull Collection<ConfigEntry<T>> entrySet, @NotNull YamlConfiguration yml) {
        for (ConfigEntry<T> entry : entrySet)
            try {
                validateEntry(configFile, entry, yml, true);
            } catch (IOException e) {
                logger.warning("IOException (problem with deleting/renaming file) - " + configFile.getAbsolutePath());
            }
    }

    protected <T> void validateEntry(@NotNull ConfigFile parent, @NotNull ConfigEntry<T> entry, YamlConfiguration yml, boolean revertWhenIncorrect) throws IOException {
        if (!entry.validate(yml)) {
            logger.severe("Value of " + entry.getName() + " in " + ConfigUtils.getFilePathFromConfig(parent, plugin.getName()) + " is wrong.");

            if (revertWhenIncorrect) {
                revertOriginal(parent, plugin);
                yml = YamlConfiguration.loadConfiguration(parent);
            }
        }

        entry.setValue((T) yml.get(entry.getName()));
    }
}
