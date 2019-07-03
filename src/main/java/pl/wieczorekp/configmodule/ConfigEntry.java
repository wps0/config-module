package pl.wieczorekp.configmodule;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfigEntry<T> {
    private static IConfigurableJavaPlugin _rootInstance;
    private String path;
    private T value;
    private String name;

    /**
     *
     * @param name Name of the variable in specified in path config file.
     * @param path Path to the file containing variable with defined name.
     * @param value Value of the variable.
     * @param _rootInstance Reference to the instance class.
     *                      If null, reference will be obtained from IConfigurableJavaPlugin static method getInstance() when needed.
     */
    public ConfigEntry(String name, String path, T value, IConfigurableJavaPlugin _rootInstance) {
        this.name = name;
        this.path = path;
        this.value = value;
        if (ConfigEntry._rootInstance == null) {
            if (_rootInstance == null)
                ConfigEntry._rootInstance = IConfigurableJavaPlugin.getInstance();
            else
                ConfigEntry._rootInstance = _rootInstance;
        }
    }

    public ConfigEntry(String name, String path) {
        this(name, path, null, IConfigurableJavaPlugin.getInstance());
    }

    public String getPath() {
        return path;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
    public static IConfigurableJavaPlugin getRootInstance() {
        return _rootInstance;
    }
    public static void setRootInstance(IConfigurableJavaPlugin _rootInstance) {
        if (ConfigEntry._rootInstance == null)
            ConfigEntry._rootInstance = _rootInstance;
    }

    public boolean validate(@NotNull YamlConfiguration yml) {
        if (value == null)
            return false;

        Class<T> clazz = (Class<T>) value.getClass();
        if (clazz.isInstance(Integer.MAX_VALUE))
            return yml.isInt(getPathInFile(this));
        if (clazz.isInstance(Boolean.FALSE))
            return yml.isBoolean(getPathInFile(this));
        if (clazz.isInstance("."))
            return yml.isString(getPathInFile(this));
        if (clazz.isInstance(Language.POLISH) || clazz.isInstance(Language.ENGLISH)) {
            for (Language language : Language.values())
                if (!yml.isString(language.getId() + "." + getPathInFile(this)))
                    return false;
            return true;
        }

        return false;
    }

    public boolean is(Class<?> object) {
        if (value == null)
            return object == null;
        return object.getTypeName().equalsIgnoreCase(value.getClass().getTypeName());
    }

    public static String getPathInFile(ConfigEntry entry) {
        if (entry.getPath().indexOf('/') == -1)
            return getPathInFile(entry.getPath() + "/" + entry.getName());
        return getPathInFile(entry.getPath());
    }

    @Deprecated
    public static String getPathInFile(String path) {
        int index = path.lastIndexOf("/") + 1;
        return index == 0 ? null : path.substring(index);
    }
}
