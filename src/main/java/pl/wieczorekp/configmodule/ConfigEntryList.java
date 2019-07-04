package pl.wieczorekp.configmodule;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConfigEntryList {
    private ArrayList<ConfigEntry<Integer>> integers;
    private ArrayList<ConfigEntry<Boolean>> booleans;
    private ArrayList<ConfigEntry<String>> strings;
    private ArrayList<ConfigEntry<Object>> objects;

    public ConfigEntryList(@NotNull ArrayList<ConfigEntry<Integer>> integers, @NotNull ArrayList<ConfigEntry<Boolean>> booleans,
                           @NotNull ArrayList<ConfigEntry<String>> strings, @NotNull ArrayList<ConfigEntry<Object>> objects) {
        this.integers = integers;
        this.booleans = booleans;
        this.strings = strings;
        this.objects = objects;
    }

    public ConfigEntryList() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public <T> void add(@NotNull ConfigEntry<T> value) {
        if (value.is(Boolean.class)) {
            booleans.add((ConfigEntry<Boolean>) value);
            return;
        }

        if (value.is(Integer.class)) {
            integers.add((ConfigEntry<Integer>) value);
            return;
        }

        if (value.is(String.class)) {
            strings.add((ConfigEntry<String>) value);
            return;
        }

        objects.add((ConfigEntry<Object>) value);
    }

    public void addInteger(@NotNull ConfigEntry<Integer> value) {
        integers.add(value);
    }

    public void addBoolean(@NotNull ConfigEntry<Boolean> value) {
        booleans.add(value);
    }

    public void addString(@NotNull ConfigEntry<String> value) {
        strings.add(value);
    }

    public void addObject(@NotNull ConfigEntry<Object> value) {
        objects.add(value);
    }

    public ArrayList<ConfigEntry<Integer>> getIntegers() {
        return integers;
    }

    public ArrayList<ConfigEntry<Boolean>> getBooleans() {
        return booleans;
    }

    public ArrayList<ConfigEntry<String>> getStrings() {
        return strings;
    }

    public ArrayList<ConfigEntry<Object>> getObjects() {
        return objects;
    }

    public int size() {
        return integers.size() + booleans.size() + strings.size() + objects.size();
    }

    public static <T> ArrayList<ConfigEntry<T>> makeList(ConfigEntry<T>... entries) {
        return new ArrayList<>(Arrays.asList(entries));
    }
}
