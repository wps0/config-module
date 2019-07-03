package pl.wieczorekp.configmodule;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 *     A single configuration property. <br />
 *     Has the list defined to hold multiple object of this type - {@link ConfigEntryList ConfigEntryList}.
 *     ToDo: Tied up with {@link FilesystemWatcher FilesystemWatcher} allows of dynamic refresh of value field
 * </p>
 *
 * ToDo: WatchService for synchronization of values!
 *
 * @param <T> Type of the value stored.
 * @version 1.0
 */
public class ConfigEntry<T> {

    /**
     * The compiled pattern used in detecting path in file.
     */
    @Getter private final static Pattern pathInFilePattern = Pattern.compile("((yml|txt)\\/?(.*)?)$");

    /**
     * The reference to instance of the JavaPlugin.
     */
//    private static IConfigurableJavaPlugin _rootInstance;

    /**
     * The path to the file containing this setting in filesystem.
     */
    @Getter private String path;

    /**
     * The path to the property in the file.
     */
    @Getter private String name;

    /**
     * The value of held property.
     */
    @Getter @Setter
    private T value;

    /**
     *
     * @param name Path to the property in file.
     * @param path Path to the file containing variable with defined path.
     * @param value Value of the property.
     *                      If null, reference will be obtained from IConfigurableJavaPlugin static method getInstance() when needed.
     */
    public ConfigEntry(String name, String path, T value) {
        this.name = name;
        this.path = path;
        this.value = value;
    }

    public ConfigEntry(String name, String path) {
        this(name, path, null);
    }


    public boolean validate(@NotNull YamlConfiguration yml) {
        if (value == null)
            return false;

        String path = getPathInFile(this);
        if (path == null)
            return false;

        Class<T> clazz = (Class<T>) value.getClass();
        if (clazz.isInstance(Integer.MAX_VALUE))
            return yml.isInt(path);
        if (clazz.isInstance(Boolean.FALSE))
            return yml.isBoolean(path);
        if (clazz.isInstance("."))
            return yml.isString(path);
        if (clazz.isInstance(Language.POLISH) || clazz.isInstance(Language.ENGLISH)) {
            for (Language language : Language.values())
                if (!yml.isString(language.getId() + "." + path))
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


    public static String getPathInFile(@NotNull ConfigEntry entry) {
        Matcher matcher = pathInFilePattern.matcher(entry.getPath());
        if (!matcher.find())
            return null;

        String group = matcher.group(3);
        if (!group.equals(""))
            group += ".";

        return group + entry.getName();
    }

    @Deprecated
    public static String getPathInFile(String path) {
        int index = path.lastIndexOf("/") + 1;
        return index == 0 ? null : path.substring(index);
    }
}
