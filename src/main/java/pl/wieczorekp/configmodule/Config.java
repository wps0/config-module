package pl.wieczorekp.configmodule;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class Config extends ConfigValidator {
    private HashMap<String, String> messages;
    private HashMap<String, Object> database;
    private File dataFolder;
    private Language language;

    // ToDo: add databaseUsed variable or sth
    public Config(@NotNull String packageName, @NotNull ConfigEntryList configEntryList, @Nullable YamlConfiguration msgFile) {
        super(IConfigurableJavaPlugin.getInstance(packageName), configEntryList);
        this.dataFolder = _rootInstance.getDataFolder();
        this.database = new HashMap<>(6);
        /*if (msgFile == null && _rootInstance.getResource("messages.yml") != null)
            msgFile = YamlConfiguration.loadConfiguration(new InputStreamReader(_rootInstance.getResource("messages.yml")));

        if (msgFile != null) {
            for (String str : msgFile.getKeys(true)) {
                System.out.println(str);
            }
        }*/
    }

    public Config(@NotNull String packageName, @NotNull ConfigEntryList configEntryList) {
        this(packageName, configEntryList, null);
    }

    public boolean load() {
        if (!this.load())
            return false;

        /*for (ConfigEntry entry : this.configEntryList) {
            String path = entry.getPath();

            if (path.contains("$")) {
                String fName = getFilePathFromConfig(path);
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(_rootInstance.getDataFolder() + "/" + fName));

                values.put(language.getPath() + path, yml.get(language.getPath() + path.substring(path.indexOf('$') + 1))); // update hash table 'values'
                entry.setValue(yml.get(language.getPath() + path.substring(path.indexOf('$') + 1))); // update entry's content

                if (values.get(language.getPath() + path) == null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix + "Message with id '" + entry.getName() + "' does not exists! Messages' file will be reset - the old one will be saved to messages.yml.old");
                    File messagesFile = new File(dataFolder, fName);
                    File messagesFileOld = new File(dataFolder, fName + ".old");

                    if (!messagesFile.renameTo(messagesFileOld)) {
                        if (!messagesFileOld.delete() || !messagesFile.renameTo(messagesFileOld)) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix + "Write operation failed (inappropriate user permissions?)");
                            return false;
                        }
                    }
                    _rootInstance.saveResource(fName, true);
                    yml = YamlConfiguration.loadConfiguration(messagesFile);
                }

                values.put(path, yml.get(language.getPath() + ConfigEntry.getPathInFile(path)));
            } else {
                values.put(path, _config.get(path));

                if (entry.getName().equalsIgnoreCase("language")) {
                    language = Language.fromStringCode((String) values.get("language"));
                    if (language == null) {
                        _rootInstance.getLogger().log(Level.WARNING, "Wrong language! Using ENGLISH");
                        language = Language.ENGLISH;
                    }
                }
            }
        }

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

        return true;
    }

    // ToDo: w config entry name to bedzie sciezka w pliku, a path - sciezka do pliku.
    //  moze jakies enumy da rade ogarnac dla kazdego pliku, ktory ma byc odczytywany czy cos,
    public Object getValue(String key) {
        return key;
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public void sendMessage(String key, CommandSender destination) {
        sendMessage(key, destination, null);
    }

    public void sendMessage(String key, CommandSender receiver, ArrayDeque<Map.Entry<String, String>> replace) {
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

        for (Map.Entry<String, String> replaceEntry : replace) {
            message = message.replace(replaceEntry.getKey(), replaceEntry.getValue());
        }

        receiver.sendMessage(message);
    }

    public HashMap<String, Object> getDatabaseCred() {
        return database;
    }

    //ToDo: przerobić na lambde!!!
    @Override
    protected boolean additionalBeforeValidation() {return true;}

    @Override
    protected boolean additionalAfterValidation() {return true;}
}
