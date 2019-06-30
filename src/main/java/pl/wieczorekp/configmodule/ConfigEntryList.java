package pl.wieczorekp.configmodule;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigEntryList {
    private ArrayList<ConfigEntry<Integer>> integers;
    private ArrayList<ConfigEntry<Boolean>> booleans;
    private ArrayList<ConfigEntry<String>> strings;
    private ArrayList<ConfigEntry<Object>> objects;

    public ConfigEntryList(ArrayList<ConfigEntry<Integer>> integers, ArrayList<ConfigEntry<Boolean>> booleans, ArrayList<ConfigEntry<String>> strings, ArrayList<ConfigEntry<Object>> objects) {
        this.integers = integers;
        this.booleans = booleans;
        this.strings = strings;
        this.objects = objects;
    }

    public ConfigEntryList() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public <T> void add(ConfigEntry<T> value) {
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

    public void addInteger(ConfigEntry<Integer> value) {
        integers.add(value);
    }

    public void addBoolean(ConfigEntry<Boolean> value) {
        booleans.add(value);
    }

    public void addString(ConfigEntry<String> value) {
        strings.add(value);
    }

    public void addObject(ConfigEntry<Object> value) {
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

    public static <T> ArrayList<ConfigEntry<T>> makeList(ConfigEntry<T>... entries) {
        return new ArrayList<>(Arrays.asList(entries));
    }
}
