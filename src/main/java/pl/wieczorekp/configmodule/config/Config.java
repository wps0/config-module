package pl.wieczorekp.configmodule.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.wieczorekp.configmodule.*;
import pl.wieczorekp.configmodule.config.filesystem.ConfigFile;
import pl.wieczorekp.configmodule.config.filesystem.FilesystemWatcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

// ToDo: chyba że jakoś annotations dodawać tam, gdzie problem jest z czmyś do rozwiązania w normalny sposób, np. przy tych typach w config entry
//      czy coś

// ToDo: zrobic config i wywalic opcje disableLiveReload: ["pluginName"]

public class Config extends ConfigValidator {
    private final FilesystemWatcher fsWatcher;
    @Setter @Getter
    private Language language;
    private Thread watcherThread;
    private boolean usePrefix;

    // ToDo: add databaseUsed variable or sth
    // ToDo: tworzenie pustych directory
    public Config(boolean usePrefix, @NotNull IConfigurableJavaPlugin mainPluginClass, @NotNull ConfigFile... configFiles) throws IOException {
        super(mainPluginClass, configFiles);
        this.usePrefix = usePrefix;
        this.language = Language.ENGLISH;
        this.fsWatcher = new FilesystemWatcher(plugin);
        this.watcherThread = new Thread(this.fsWatcher);
    }

    @Override
    public boolean load() {
        logger.info("Loading config...");
        try {
            if (!super.load())
                return false;
        } catch (IOException e) {
            logger.fine("IO Exception!");
            logger.fine("Message: " + e.getMessage());
            logger.finer("Stack trace: " + Arrays.toString(e.getStackTrace()));
            return false;
        }

        String lang = getValue("language");
        if (lang != null)
            language = Language.fromStringCode(lang);

        if (language == null)
            language = Language.ANY;

        try {
            Thread.sleep(500);
            fsWatcher.init(plugin.getDataFolder().toPath());
            watcherThread.setName(plugin.getName().replaceAll("\\s", "") + "-ConfigWatcher");
            watcherThread.start();
        } catch (IOException | InterruptedException e) {
            // ToDo: zmienic sposob wyswietlania wiadomosci!!
            logger.warning("Could not init filesystem watcher! Live config reloading is disabled.");
        }

        return true;
    }

    public void loadMessages(Language language, String[] ids) {
        loadValues(ConfigFile.toArray(messageFiles), ids, language.getId());
    }

    public void reload(@NotNull Path parent, @NotNull Path child) throws FileNotFoundException {
        ConfigFile found = null;
        Path filePath = parent.resolve(child).toAbsolutePath();

        synchronized (this) {
            for (ConfigFile configFile : configFiles.values()) {
                if (configFile.getAbsolutePath().equals(filePath.toString())) {
                    found = configFile;
                    break;
                }
            }
        }
        if (found == null)
            throw new FileNotFoundException("could not find file with given path");
        reload(found);
    }

    public void reload(@NotNull ConfigFile configFile) {
        plugin.getLogger().info("Reloading file " + configFile.getPath() + "...");
        if (!configFile.canReload())
            throw new IllegalStateException("cannot reload file as it was reloaded not long ago");

        synchronized (this) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
            for (ConfigEntry<?> entry : (Set<ConfigEntry<?>>) configFile.getEntries().values()) {
                try {
                    validateEntry(configFile, entry, yml, false);
                } catch (IOException e) {
                    logger.warning("Wrong value for " + entry.getName() + ". Cannot delete/rename file.");
                }
            }

            configFile.updateLastReload();
        }
    }

    public void cleanup() {
        if (!watcherThread.isInterrupted())
            watcherThread.interrupt();
    }

    @Nullable
    public <T> T getValue(@NotNull String key) {
        return get(key, configFiles.values());
    }

    @Nullable
    public String getMessage(@NotNull String key) {
        Object msg = get(language.getPath() + key, messageFiles);
        return msg != null ? String.valueOf(msg) : null;
    }

    @Nullable
    private <T> T get(@NotNull String key, @NotNull Collection<ConfigFile> files) {
        for (ConfigFile cf : files) {
            ConfigEntry<T> entry;
            if ((entry = cf.getEntries().get(key)) != null) {
                T result = entry.getValue();
                return result != null ? ;
            }
        }
        return null;
    }

    public void sendMessage(@NotNull CommandSender receiver, @NotNull String key, ReplaceEntry... replace) {
        StringBuilder sb = new StringBuilder(usePrefix ? prefix + " " : "");
        String message = getMessage(key);

        if (message == null)
            sb.append(ChatColor.RED).append("Unknown message");

        if (replace != null && message != null)
            for (ReplaceEntry replaceEntry : replace)
                message = message.replace(replaceEntry.getKey(), replaceEntry.getValue());

        sb.append(message);

        receiver.sendMessage(sb.toString().replace('&', ChatColor.COLOR_CHAR));
    }
}
