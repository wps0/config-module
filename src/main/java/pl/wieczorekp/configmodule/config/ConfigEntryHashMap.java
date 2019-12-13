package pl.wieczorekp.configmodule.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class ConfigEntryHashMap implements Map {
    private Map<Object, ConfigEntry<Integer>> integers;
    private Map<Object, ConfigEntry<Boolean>> booleans;
    private Map<Object, ConfigEntry<String>> strings;
    private Map<Object, ConfigEntry<Object>> objects;

    public ConfigEntryHashMap(@NotNull Map<Object, ConfigEntry<Integer>> integers, @NotNull Map<Object, ConfigEntry<Boolean>> booleans,
                              @NotNull Map<Object, ConfigEntry<String>> strings, @NotNull Map<Object, ConfigEntry<Object>> objects) {
        this.integers = integers;
        this.booleans = booleans;
        this.strings = strings;
        this.objects = objects;
    }

    public ConfigEntryHashMap() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    @Override
    public int size() {
        return integers.size() + booleans.size() + strings.size() + objects.size();
    }

    @Override
    public boolean isEmpty() {
        return integers.size() + booleans.size() + strings.size() + objects.size() == 0;
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        return integers.containsKey(key) || booleans.containsKey(key) || strings.containsKey(key) || objects.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull Object value) {
        return integers.containsValue(value) || booleans.containsValue(value) || strings.containsValue(value) || objects.containsValue(value);
    }

    @Override
    public ConfigEntry<?> get(@NotNull Object key) {
        ConfigEntry<?> result = integers.get(key);
        if (result == null) {
            result = booleans.get(key);
            if (result == null) {
                result = strings.get(key);
                if (result == null)
                    result = objects.get(key);
            }
        }
        return result;
    }

    public <T> ConfigEntry<T> get(@NotNull String key) {
        return (ConfigEntry<T>) get((Object) key);
    }

    @Nullable
    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException("put operation is not supported");
        /*if (!(value instanceof ConfigEntry))
            throw new ClassCastException("value has to inherit ConfigEntry<?>");

        ConfigEntry<?> val = (ConfigEntry<?>) value;

        Object prevValue;
        if (val.is(Integer.class))
            prevValue = integers.put(key, (ConfigEntry<Integer>) val);
        else if (val.is(Boolean.class))
            prevValue = booleans.put(key, (ConfigEntry<Boolean>) val);
        else if (val.is(String.class))
            prevValue = strings.put(key, (ConfigEntry<String>) val);
        else
            prevValue = objects.put(key, (ConfigEntry<Object>) val);
        return prevValue;*/
    }

    public ConfigEntry<String> put(String key, ConfigEntry<String> value) {
        return strings.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("remove operation is not supported");
    }

    @Override
    public void putAll(@NotNull Map m) {
        throw new UnsupportedOperationException("putAll operation is not supported");
    }

    @Override
    public void clear() {
        integers.clear();
        booleans.clear();
        strings.clear();
        objects.clear();
    }

    @NotNull
    @Override
    public Set<?> keySet() {
        HashSet<Object> keySet = new HashSet<>();
        keySet.addAll(integers.keySet());
        keySet.addAll(booleans.keySet());
        keySet.addAll(strings.keySet());
        keySet.addAll(objects.keySet());
        return keySet;
    }

    @NotNull
    @Override
    public Collection values() {
        HashSet<ConfigEntry<?>> values = new HashSet<>();
        values.addAll(integers.values());
        values.addAll(booleans.values());
        values.addAll(strings.values());
        values.addAll(objects.values());
        return values;
    }

    @NotNull
    @Override
    public Set<Entry> entrySet() {
        Set<Entry> entrySet = new HashSet<>(integers.entrySet());
        entrySet.addAll(booleans.entrySet());
        entrySet.addAll(strings.entrySet());
        entrySet.addAll(objects.entrySet());

        return entrySet;
    }

    public Map<Object, ConfigEntry<Integer>> getIntegers() {
        return integers;
    }

    public Map<Object, ConfigEntry<Boolean>> getBooleans() {
        return booleans;
    }

    public Map<Object, ConfigEntry<String>> getStrings() {
        return strings;
    }

    public Map<Object, ConfigEntry<Object>> getObjects() {
        return objects;
    }

    @SafeVarargs
    @NotNull
    @Deprecated
    public static <T> HashMap<Object, ConfigEntry<T>> makeHashMap(@NotNull ConfigEntry<T>... entries) {
        HashMap<Object, ConfigEntry<T>> map = new HashMap<>();
        for (ConfigEntry<T> entry : entries)
            map.put(entry.getName(), entry);
        return map;
    }
}
