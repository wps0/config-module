package pl.wieczorekp.configmodule;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import static java.io.File.separatorChar;

public abstract class ConfigValidator {
    private boolean status;
    protected final IConfigurableJavaPlugin _rootInstance;
    protected FileConfiguration _config;
    protected File dataFolder;
    protected String prefix;
    protected Vector<ConfigEntry> configEntryList; //ToDo: hash mapa

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    ///////////////////////////////////////////////////////////////////////////
    protected ConfigValidator(String packageName, FileConfiguration cfg, File dataFolder, ConfigEntry[] paths) {
        this._rootInstance = IConfigurableJavaPlugin.getInstance(packageName);
        this._config = cfg;
        this.dataFolder = dataFolder;
        this.configEntryList = new Vector<>(Arrays.asList(paths));
        this.status = true;
        this.prefix = _rootInstance.getName() + " ";
    }

    protected ConfigValidator(FileConfiguration cfg, File dataFolder, ConfigEntry[] paths) {
        this("pl.wieczorekp", cfg, dataFolder, paths);
    }


    protected abstract boolean additionalBeforeValidation();
    protected abstract boolean additionalAfterValidation();

    public boolean validate() {
        if (!dataFolder.exists())
            status = dataFolder.mkdir();

        if (!additionalBeforeValidation())
            status = false;

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

            target.setContent(_config.get(target.getPath()));
        }

        for (ConfigEntry ce : configEntryList) {
            if (!ce.validate()) {
                status = false;
                printError(ce.getName() + (ce.is(Language.class) ? " in one of the languages" : "" ), ErrorCode.WRONG_VALUE);
            }
        }

        if (!additionalAfterValidation())
            status = false;

        return status;
    }

    ///////////////////////////////////////////////////////////////////////////
    // protected methods
    ///////////////////////////////////////////////////////////////////////////
    protected void addPath(ConfigEntry configEntry) {
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

        String message = "§4[" + this.prefix + " ERROR " + code.getError() + "] §c" + sb.toString();
        if (code.getError() > 0)
            message = "§9[" + this.prefix + " INFO " + code.getError() + "] §b" + sb.toString();

        Bukkit.getConsoleSender().sendMessage(message);
    }


    /**
     *
     * @param path
     * @return converted path to yaml file.
     */
    public static String getFilePathFromConfig(String path) {
        if (path == null)
            throw new NullPointerException("File path cannot be null!");

        char[] newPath = path.replace('$', separatorChar).toCharArray();

        if (!path.contains("$"))
            return "config.yml";

        return String.valueOf(newPath);

        /*int wp;
        char[] wpath;
        if ((wp = path.indexOf("$")) == -1) {
            return "config.yml";
        } else {
            wpath = path.substring(0, wp).toCharArray();
        }

        for (int i = 0; i < wpath.length; i++) {
            if (wpath[i] == '.')
                wpath[i] = File.separatorChar;
        }



        return new String(wpath) + ".yml";*/
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
