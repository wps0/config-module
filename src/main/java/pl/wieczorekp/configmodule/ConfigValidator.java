package pl.wieczorekp.configmodule;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static java.io.File.separatorChar;

public abstract class ConfigValidator {
    private boolean status;
    protected IConfigurableJavaPlugin _rootInstance;
    protected FileConfiguration _config;
    protected File dataFolder;
    protected String prefix;
    protected ConfigEntryList configEntryList; //ToDo: hash mapa

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    ///////////////////////////////////////////////////////////////////////////
    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, FileConfiguration _config, File dataFolder, String prefix, ConfigEntryList paths) {
        this._rootInstance = _rootInstance;
        this._config = _config;
        this.dataFolder = dataFolder;
        this.prefix = prefix;
        this.configEntryList = paths;
    }

    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, ConfigEntryList paths) {
        this(_rootInstance, _rootInstance.getConfig(), _rootInstance.getDataFolder(), _rootInstance.getName() + " ", paths);
    }

    protected abstract boolean additionalBeforeValidation();
    protected abstract boolean additionalAfterValidation();

    public boolean validate() {
        if (!dataFolder.exists())
            status = dataFolder.mkdir();

        if (!additionalBeforeValidation())
            status = false;

        if (configEntryList == null)
            return true;

        YamlConfiguration yml = null;
        String prevPath = null;

        for (ConfigEntry<Boolean> entry : configEntryList.getBooleans())
            validateEntry(entry, prevPath, yml);

        for (ConfigEntry<Integer> entry : configEntryList.getIntegers())
            validateEntry(entry, prevPath, yml);

        for (ConfigEntry<String> entry : configEntryList.getStrings())
            validateEntry(entry, prevPath, yml);

        /*
        boolean configExists = false;
        if (!new File(dataFolder, "config.yml").exists()) {
            if (_rootInstance.getResource("config.yml") != null) {
                _rootInstance.saveDefaultConfig();
                this._config = _rootInstance.getConfig();
            } else
                configExists = true;
        }

        if (!new File(dataFolder, "messages.yml").exists() && _rootInstance.getResource("messages.yml") != null)
            _rootInstance.saveResource("messages.yml", false);

        for (ConfigEntry target : configEntryList) {
            if (target.getRootInstance() == null)
                target.setRootInstance(_rootInstance);

            if (target.getPath().contains("$")) {
                String customFilePath = getFilePathFromConfig(target.getPath());
                File f = new File(_rootInstance.getDataFolder() + String.valueOf(separatorChar) + customFilePath);

                if (!f.exists()) {
                    printError(customFilePath, ErrorCode.NOT_EXISTED);

                    _rootInstance.saveResource(customFilePath, false);
                    if (!f.exists()) {
                        printError(customFilePath, ErrorCode.FILE_CREATION_ERROR);
                        status = false;
                    }
                }
                continue;
            }

            if (!configExists && !_config.contains(target.getPath())) {
                status = false;
                if (!_config.isSet(target.getPath())) {
                    printError(target.getPath(), ErrorCode.NOT_SET);
                    continue;
                }
                printError(target.getPath(), ErrorCode.NOT_EXIST);
                continue;
            }

            target.setValue(_config.get(target.getPath()));
        }

        for (ConfigEntry ce : configEntryList) {
            if (!ce.validate()) {
                status = false;
                printError(ce.getName() + (ce.is(Language.class) ? " in one of the languages" : "" ), ErrorCode.WRONG_VALUE);
            }
        }
        */
        if (!additionalAfterValidation())
            status = false;

        return status;

    }

    private <T> void validateEntry(ConfigEntry<T> entry, String prevPath, YamlConfiguration yml) {
        String filePath = getFilePathFromConfig(entry.getPath());
        File f = createFileFromPath(_rootInstance, filePath);

        // ToDo: usunac te wiadomosci, bo useless ogolnie
        if (!f.exists()) {
            printError(filePath, ErrorCode.NOT_EXISTED);

            _rootInstance.saveResource(filePath, false);
            if (!f.exists()) {
                printError(filePath, ErrorCode.FILE_CREATION_ERROR);
                status = false;
            }
        }

        System.out.println(f.getAbsolutePath());
        if (!f.getAbsolutePath().equals(prevPath))
            yml = YamlConfiguration.loadConfiguration(f);

        entry.is(yml.get(entry.getName()).getClass());

        prevPath = f.getAbsolutePath();
    }

    ///////////////////////////////////////////////////////////////////////////
    // protected methods
    ///////////////////////////////////////////////////////////////////////////
    protected <T> void addPath(ConfigEntry<T> configEntry) {
        configEntryList.add(configEntry);
    }

    protected void printError(String value, ErrorCode code) {
        StringBuilder sb = new StringBuilder();
        sb.append("Value of '").append(value).append("' ");

        switch (code) {
            case NOT_EXISTED:
                sb.append("hadn't existed, but was auto-created");
                break;
            case NOT_EXIST:
                sb.append("does not exist");
                break;
            case NOT_SET:
                sb.append("is not set");
                break;
            case WRONG_VALUE:
                sb.append("is wrong");
                break;
            case FILE_CREATION_ERROR:
                sb.append("couldn't create file");
                break;
            default:
                sb.append("error message not set");
                break;
        }

        String message = "§4[" + this.prefix + "ERROR " + code.getError() + "] §c" + sb.toString();
        if (code.getError() > 0)
            message = "§9[" + this.prefix + "INFO " + code.getError() + "] §b" + sb.toString();

        Bukkit.getConsoleSender().sendMessage(message);
    }


    /**
     *
     * @param path Path received from ConfigEntry.
     * @return Path to yml file.
     */
    public static String getFilePathFromConfig(String path) {
        System.out.println("getFilePathFromConfig input: " + path);
        if (path == null)
            throw new NullPointerException("File path cannot be null!");

        if (!path.contains("/"))
            return path;

        int lastIndex = path.replace('/', separatorChar).trim().lastIndexOf(separatorChar);
        return path.substring(0, lastIndex == -1 ? 0 : lastIndex);
    }

    public static File createFileFromPath(IConfigurableJavaPlugin instance, String path) {
        return new File(instance.getDataFolder() + String.valueOf(separatorChar) + path);
    }

    protected enum ErrorCode {
        NOT_EXISTED(1),
        NOT_EXIST(-1),
        NOT_SET(-2),
        WRONG_VALUE(-3),
        FILE_CREATION_ERROR(-4);

        private int error;
        ErrorCode(int code) {
            this.error = code;
        }

        public int getError() {
            return error;
        }
    }
}
