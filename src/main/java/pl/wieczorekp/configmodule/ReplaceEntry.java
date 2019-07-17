package pl.wieczorekp.configmodule;

import java.util.Map;

public class ReplaceEntry implements Map.Entry {
    private String key;
    private String value;

    /**
     * Constants
     */
    public ReplaceEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String setValue(Object value) {
        if (value instanceof String) {
            this.value = (String) value;
            return this.value;
        }
        return null;
    }
}
