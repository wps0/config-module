package pl.wieczorekp.configmodule;

import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Map.Entry, but key and value are Strings.
 * Used in {@link pl.wieczorekp.configmodule.config.Config#sendMessage(CommandSender, String, ReplaceEntry...)}
 *
 * @author wps0
 * @since 1.1.0
 * @see java.util.Map.Entry
 * @see pl.wieczorekp.configmodule.config.Config
 */
public class ReplaceEntry implements Map.Entry {
    private String key;
    private String value;

    /**
     * Default constructor.
     *
     * @param key Key
     * @param value Value
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
