package pl.wieczorekp.configmodule;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 *     A single configuration property. <br />
 *     ToDo: Tied up with {@link FilesystemWatcher FilesystemWatcher} allows of dynamic refresh of value field
 *      add flag 'dynamic refresh' or sth. jesli tak, to za kazda zmiana sle do pluginu sygnal, zeby odswierzyc. moze bez sensu to jest? w sumie zjada tylko resource.
 *      domyslnie na false niech
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
    @Getter private final static Pattern pathInFilePattern = Pattern.compile("((yml|txt)/?(.*)?)$");

    /**
     * The path to the property in the file.
     */
    @Getter private String name;

    /**
     * The value of the held property.
     */
    @Getter @Setter
    private T value;

    /**
     *
     * @param name Path to the property in file.
     * @param value Value of the property.
     */
    public ConfigEntry(@NotNull String name, @Nullable T value) {
        this.name = name;
        this.value = value;
    }

    public ConfigEntry(@NotNull String name) {
        this(name, null);
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

    public boolean is(@Nullable Class<?> object) {
        if (object == null)
            return value == null;
        if (value == null)
            return false;

        return object.getTypeName().equalsIgnoreCase(value.getClass().getTypeName());
    }


    @Nullable
    @Deprecated
    public static String getPathInFile(@NotNull ConfigEntry entry) {
        return entry.getName();
    }
}
