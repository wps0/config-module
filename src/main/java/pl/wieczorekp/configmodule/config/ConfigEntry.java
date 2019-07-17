package pl.wieczorekp.configmodule.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.wieczorekp.configmodule.Language;

import java.util.function.Supplier;
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
    @Getter private final static Pattern pathInFilePattern = Pattern.compile("(.*)(.*)");

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
     * Container for a class type
     */
    private Supplier<T> supplier;

    public ConfigEntry(@NotNull String name, @Nullable T value, Supplier<T> type) {
        this.name = name;
        this.value = value;
        this.supplier = type;
    }

    public ConfigEntry(String name, Supplier<T> supplier) {
        this(name, null, supplier);
    }

    /**
     *
     * @param yml
     * @return <code>true</code> if only is the value correct, <code>false</code> otherwise.
     */
    public boolean validate(@NotNull YamlConfiguration yml) {
        if (supplier == null || name == null)
            return false;

        T val = value;

        try {
            if (val == null)
                val = (T) yml.get(name);

            if (supplier.get() instanceof Language) {
                for (Language language : Language.values())
                    if (language.getId().equalsIgnoreCase(String.valueOf(val)))
                        return true;
                return false;
            }

            if (val.getClass().isInstance(supplier.get()))
                return true;
        } catch (ClassCastException | NullPointerException e) {
            return false;
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
}
