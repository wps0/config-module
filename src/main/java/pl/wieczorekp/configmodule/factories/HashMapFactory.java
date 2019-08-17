package pl.wieczorekp.configmodule.factories;

import org.jetbrains.annotations.NotNull;
import pl.wieczorekp.configmodule.config.ConfigEntry;
import pl.wieczorekp.configmodule.config.ConfigEntryHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link HashMapFactory} contains methods useful for creating HashMaps required i.a. in {@link ConfigEntryHashMap#ConfigEntryHashMap(Map, Map, Map, Map)}
 *
 * @author wps0
 * @since 1.1.2
 * @see HashMap
 */
public class HashMapFactory {
    @SafeVarargs
    @NotNull
    public static <T> HashMap<String, ConfigEntry<T>> getConfigEntryHashMap(ConfigEntry<T>... entries) {
        HashMap<String, ConfigEntry<T>> map = new HashMap<>();
        for (ConfigEntry<T> entry : entries)
            map.put(entry.getName(), entry);
        return map;
    }

    @SafeVarargs
    @NotNull
    public static <K, V> HashMap<K, V> getHashMap(Map.Entry<K, V>... entries) {
        HashMap<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());
        return map;
    }

    @SafeVarargs
    @NotNull
    @Deprecated
    public static <T> HashMap<Object, ConfigEntry<T>> makeHashMap(ConfigEntry<T>... entries) {
        HashMap<Object, ConfigEntry<T>> map = new HashMap<>();
        for (ConfigEntry<T> entry : entries)
            map.put(entry.getName(), entry);
        return map;
    }
}
