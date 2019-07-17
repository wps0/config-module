package pl.wieczorekp.configmodule.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.wieczorekp.configmodule.IConfigurableJavaPlugin;
import pl.wieczorekp.configmodule.Language;
import pl.wieczorekp.configmodule.ReplaceEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class Config extends ConfigValidator {
    private final FilesystemWatcher fsWatcher;
    @Setter @Getter
    private Language language;
    private Thread watcherThread;

    // ToDo: add databaseUsed variable or sth
    // ToDo: tworzenie pustych directory
    public Config(@NotNull String packageName, @NotNull ConfigFile... configFiles) throws IOException {
        super(IConfigurableJavaPlugin.getInstance(packageName), configFiles);
        this.language = Language.ENGLISH;
        this.fsWatcher = new FilesystemWatcher(_rootInstance);
        this.watcherThread = new Thread(this.fsWatcher);
    }

    @Override
    public boolean load() {
        System.out.println("Loading config...");
        if (!super.load())
            return false;

        String lang = getValue("language");
        if (lang != null)
            language = Language.fromStringCode(lang);

        if (language == null)
            language = Language.ANY;

        try {
            Thread.sleep(500);
            fsWatcher.init(dataFolder.toPath());
            watcherThread.setName(this._rootInstance.getName().replaceAll("\\s", "") + "-ConfigWatcher");
            watcherThread.start();
        } catch (IOException | InterruptedException e) {
            // ToDo: zmienic sposob wyswietlania wiadomosci!!
            logger.warning("Could not init filesystem watcher! Live config reloading is disabled.");
        }

        return true;

        /*
        database.put("type", _config.getInt("database.type"));
        database.put("address", _config.getString("database.address"));
        database.put("port", _config.getString("database.port"));
        database.put("login", _config.getString("database.login"));
        database.put("password", _config.getString("database.password"));
        database.put("name", _config.getString("database.name"));

        if ((int) database.get("type") == 0) {
            printError("database.type", ErrorCode.WRONG_VALUE);
            return false;
        }*/
    }

    public void reload(@NotNull Path parent, @NotNull Path child) throws FileNotFoundException {
        ConfigFile found = null;
        Path filePath = parent.resolve(child).toAbsolutePath();
        synchronized (this) {
            for (ConfigFile configFile : configFiles.values()) {

                if (configFile.getAbsolutePath().equals(filePath.toString())) {
                    System.out.format("wsm to true dla: %s", child.toString());
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
        if (!configFile.canReload())
            throw new IllegalStateException("cannot reload file as it was reloaded not long ago");

        synchronized (this) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
            for (ConfigEntry<?> entry : (Set<ConfigEntry<?>>) configFile.getEntries().values())
                validateEntry(configFile, entry, yml, true, false);

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
            if ((entry = cf.getEntries().get(key)) != null)
                return entry.getValue();
        }
        return null;
    }

    public void sendMessage(@NotNull CommandSender receiver, @NotNull String key, ReplaceEntry... replace) {
        String message = getMessage(key);

        if (message == null) {
            receiver.sendMessage(ChatColor.RED + "Unknown message");
            return;
        }

        message = message.replace('&', ChatColor.COLOR_CHAR);
        if (replace == null) {
            receiver.sendMessage(message);
            return;
        }

        for (ReplaceEntry replaceEntry : replace)
            message = message.replace(replaceEntry.getKey(), replaceEntry.getValue());

        receiver.sendMessage(message);
    }
}
