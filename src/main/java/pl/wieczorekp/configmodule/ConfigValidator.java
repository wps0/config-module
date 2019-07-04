package pl.wieczorekp.configmodule;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static java.io.File.separatorChar;

public abstract class ConfigValidator {
    private boolean status;
    protected IConfigurableJavaPlugin _rootInstance;
    protected File dataFolder;
    protected String prefix;
    protected ConfigEntryList configEntryList;

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    ///////////////////////////////////////////////////////////////////////////
    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, File dataFolder, String prefix, ConfigEntryList paths) {
        this._rootInstance = _rootInstance;
        this.dataFolder = dataFolder;
        this.prefix = prefix;
        this.configEntryList = paths;
    }

    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, ConfigEntryList paths) {
        this(_rootInstance, _rootInstance.getDataFolder(), _rootInstance.getName() + " ", paths);
    }

    protected abstract boolean additionalBeforeValidation();
    protected abstract boolean additionalAfterValidation();

    public boolean load() {
        if (!dataFolder.exists())
            status = dataFolder.mkdir();

        if (!additionalBeforeValidation())
            status = false;

        if (configEntryList == null || configEntryList.size() == 0)
            return true;

//        if (ConfigEntry.getRootInstance() == null)
//            ConfigEntry.setRootInstance(_rootInstance);

        // ToDo: zoptymalizować - tworzyć tylko pliki i sprawdzać czy są na startupie

        YamlConfiguration yml = null;
        String prevPath = null;
        for (ConfigEntry<Boolean> entry : configEntryList.getBooleans())
            validateEntry(entry, prevPath, yml);

        for (ConfigEntry<Integer> entry : configEntryList.getIntegers())
            validateEntry(entry, prevPath, yml);

        for (ConfigEntry<String> entry : configEntryList.getStrings())
            validateEntry(entry, prevPath, yml);

        for (ConfigEntry<Object> entry : configEntryList.getObjects())
            validateEntry(entry, prevPath, yml);

        if (!additionalAfterValidation())
            status = false;

        return status;
    }

    ///////////////////////////////////////////////////////////////////////////
    // protected methods
    ///////////////////////////////////////////////////////////////////////////
    protected <T> void validateEntry(@NotNull ConfigEntry<T> entry, String prevPath, YamlConfiguration yml) {
        validateEntry(entry, prevPath, yml, true);
    }

    protected <T> void validateEntry(@NotNull ConfigEntry<T> entry, String prevPath, YamlConfiguration yml, boolean loadData) {
        String filePath = getFilePathFromConfig(entry.getPath());
        File f = createFileFromPath(_rootInstance, filePath);

        // ToDo: usunac te wiadomosci, bo useless ogolnie
        if (!f.exists()) {
            printError(filePath, ErrorCode.NOT_EXISTED);

            _rootInstance.saveResource(filePath, false);
            if (!f.exists()) {
                printError(filePath, ErrorCode.FILE_CREATION_ERROR);
                status = false;
                return;
            }
        }
        System.out.println(f.getAbsolutePath());
        if (!f.getAbsolutePath().equals(prevPath))
            yml = YamlConfiguration.loadConfiguration(f);

        if (loadData)
            entry.setValue((T) yml.get(ConfigEntry.getPathInFile(entry)));

        if (!entry.validate(yml)) {
            status = false;
            printError(ConfigEntry.getPathInFile(entry), ErrorCode.WRONG_VALUE);
            revertOriginal(f.getAbsolutePath());
        }

        prevPath = f.getAbsolutePath();
    }

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
//        System.out.println("getFilePathFromConfig input: " + path);
        if (path == null)
            throw new NullPointerException("File path cannot be null!");

        if (!path.contains("/"))
            return path;

        return path.substring(0, path.replace('/', separatorChar).trim().lastIndexOf(separatorChar));
    }

    public static File createFileFromPath(IConfigurableJavaPlugin instance, String path) {
        return new File(instance.getDataFolder() + String.valueOf(separatorChar) + path);
    }

    public static void revertOriginal(String path) {
        revertOriginal(path, IConfigurableJavaPlugin.getInstance());
    }

    public static void revertOriginal(String path, IConfigurableJavaPlugin _rootInstance) {
        File oldFile = new File(path);
        if (!oldFile.renameTo(new File(path + ".old")))
            return;

        _rootInstance.saveResource(path, false);
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
