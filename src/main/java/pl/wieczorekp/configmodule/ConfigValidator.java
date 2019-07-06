package pl.wieczorekp.configmodule;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ConfigValidator {
    @Setter @Getter private static Pattern pathPattern;
    private boolean status;
    protected IConfigurableJavaPlugin _rootInstance;
    protected File dataFolder;
    protected String prefix;
    protected List<@NotNull ConfigFile> configFiles;

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * ToDo: chyba że zamiast tego ConfigFiles walić pobierać typy i ogólnie wszystki z folderu głównego tego pluginu
     *  i wtedy do konstruktora walnąć, żeby otrzymywał stringa z zoot paczką, w której są config pliki
     *  zapomniałem co chciałem napisać,  ripek
     *  dzięki działa
     */
    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, File dataFolder, String prefix, @NotNull ConfigFile... files) {
        this._rootInstance = _rootInstance;
        this.dataFolder = dataFolder;
        this.prefix = prefix;
        this.configFiles = Arrays.asList(files);
        pathPattern = Pattern.compile(_rootInstance.getName() + "([/\\\\])(.+)");
    }

    protected ConfigValidator(IConfigurableJavaPlugin _rootInstance, @NotNull ConfigFile... files) {
        this(_rootInstance, _rootInstance.getDataFolder(), _rootInstance.getName() + " ", files);
    }

    protected abstract boolean additionalBeforeValidation();
    protected abstract boolean additionalAfterValidation();

    public boolean load() {
        if (!dataFolder.exists())
            status = dataFolder.mkdir();

        if (!additionalBeforeValidation())
            status = false;

        if (configFiles == null || configFiles.size() == 0)
            return true;

        for (ConfigFile configFile : configFiles) {
            if (!configFile.exists()) {
                System.out.println(configFile.getName() + " not exists!");

                String path = getFilePathFromConfig(configFile);
                if (_rootInstance.getResource(path) == null)
                    throw new IllegalArgumentException("file " + path + " does not exists in plugin's jar file");

                _rootInstance.saveResource(path, false);
                if (!configFile.exists()) {
                    status = false;
                    throw new RuntimeException("could not create file " + configFile.getPath());
                }
            } else
                System.out.println(configFile.getName() + " exists!");

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
            for (Map.Entry<Object, ConfigEntry<Integer>> entry : configFile.getEntries().getIntegers().entrySet())
                validateEntry(configFile, entry.getValue(), yml);

            for (Map.Entry<Object, ConfigEntry<Boolean>> entry : configFile.getEntries().getBooleans().entrySet())
                validateEntry(configFile, entry.getValue(), yml);

            for (Map.Entry<Object, ConfigEntry<String>> entry : configFile.getEntries().getStrings().entrySet())
                validateEntry(configFile, entry.getValue(), yml);

            for (Map.Entry<Object, ConfigEntry<Object>> entry : configFile.getEntries().getObjects().entrySet())
                validateEntry(configFile, entry.getValue(), yml);
        }

        if (!additionalAfterValidation())
            status = false;

        return status;
    }

    ///////////////////////////////////////////////////////////////////////////
    // protected methods
    ///////////////////////////////////////////////////////////////////////////
    protected <T> void validateEntry(@NotNull ConfigFile parent, @NotNull ConfigEntry<T> entry, YamlConfiguration yml) {
        validateEntry(parent, entry, yml, true);
    }

    protected <T> void validateEntry(@NotNull ConfigFile parent, @NotNull ConfigEntry<T> entry, YamlConfiguration yml, boolean loadData) {
        if (!entry.validate(yml)) {
            printError(entry.getName(), ErrorCode.WRONG_VALUE);
            revertOriginal(parent);
            yml = YamlConfiguration.loadConfiguration(parent);
        }

        if (loadData)
            entry.setValue((T) yml.get(entry.getName()));
    }

    protected void printError(String value, ErrorCode code) {
        StringBuilder sb = new StringBuilder();
        sb.append(value.replaceFirst("(.)", "$1".toUpperCase())).append(" ");

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
     * @return Relative path to the yml file.
     */
    public static String getFilePathFromConfig(ConfigFile configFile) {
        System.out.println("getFilePathFromConfig input: " + configFile.getPath());
        Matcher matcher = pathPattern.matcher(configFile.getPath());
        if (!matcher.find())
            throw new IllegalArgumentException("could not calculate path to the file");
        return matcher.group(2);
    }

//    public static File createFileFromPath(IConfigurableJavaPlugin instance, String path) {
//        return new File(instance.getDataFolder() + String.valueOf(separatorChar) + path);
//    }

    public static void revertOriginal(ConfigFile configFile) {
        revertOriginal(configFile, IConfigurableJavaPlugin.getInstance());
    }

    public static void revertOriginal(ConfigFile configFile, IConfigurableJavaPlugin _rootInstance) {
        System.out.println("ha");
        File oldFile = new File(configFile.getAbsolutePath()); // ToDo: nie bedzie czasami z .olda bralo?
        File prevOldFile = new File(configFile.getAbsolutePath() + ".old");

        if (prevOldFile.exists() && !prevOldFile.delete())
            throw new RuntimeException("cannot delete file " + prevOldFile.getName());

        if (!oldFile.renameTo(prevOldFile))
            throw new RuntimeException("cannot rename file to " + oldFile.getName());

        _rootInstance.saveResource(getFilePathFromConfig(configFile), false);

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
