package pl.wieczorekp.configmodule;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigEntry<T> {
    private IConfigurableJavaPlugin _rootInstance;
    private String path;
    private T content;
    private String name;

    public ConfigEntry(String name, String path, T content, IConfigurableJavaPlugin _rootInstance) {
        this.name = name;
        this.path = path;
        this.content = content;
        this._rootInstance = _rootInstance;
    }

    public ConfigEntry(String name, String path) {
        this(name, path, null, null);
    }

    public String getPath() {
        return path;
    }
    public void setContent(T content) {
        this.content = content;
    }
    public T getContent() {
        return content;
    }
    public String getName() {
        return name;
    }
    public IConfigurableJavaPlugin getRootInstance() {
        return _rootInstance;
    }
    public void setRootInstance(IConfigurableJavaPlugin _rootInstance) {
        this._rootInstance = _rootInstance;
    }

    public boolean is(Object object) {
        if (content == null)
            return object == null;

        Class<T> clazz = (Class<T>) content.getClass();
        return clazz.isInstance(object);
    }

    public boolean validate() {
        if (_rootInstance == null)
            _rootInstance = IConfigurableJavaPlugin.getInstance();

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(
                new File(_rootInstance.getDataFolder().getAbsolutePath() + File.pathSeparator + ConfigValidator.getFilePathFromConfig(path))
        );
        if (content == null)
            return false;

        Class<T> clazz = (Class<T>) content.getClass();
        if (clazz.isInstance(Integer.MAX_VALUE))
            return yml.isInt(getPathInFile(this.path));
        if (clazz.isInstance(Boolean.FALSE))
            return yml.isBoolean(getPathInFile(this.path));
        if (clazz.isInstance("."))
            return yml.isString(getPathInFile(this.path));
        if (clazz.isInstance(Language.POLISH) || clazz.isInstance(Language.ENGLISH)) {
            for (Language language : Language.values())
                if (!yml.isString(language.getId() + "." + getPathInFile(this.path)))
                    return false;
            return true;
        }

        return false;
    }

    public static String getPathInFile(String path) {
        return path.substring(path.indexOf("$") + 1);
    }
}
