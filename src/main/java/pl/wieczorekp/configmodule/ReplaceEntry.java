package pl.wieczorekp.configmodule;

import org.bukkit.command.CommandSender;

import java.util.AbstractMap;

/**
 * Map.Entry, but key and value are Strings.
 * Used in {@link pl.wieczorekp.configmodule.config.Config#sendMessage(CommandSender, String, ReplaceEntry...)}
 *
 * @author wps0
 * @since 1.1.0
 * @see java.util.Map.Entry
 * @see pl.wieczorekp.configmodule.config.Config
 */
@Deprecated
public class ReplaceEntry extends AbstractMap.SimpleEntry<String, String> {
    /**
     * Default constructor.
     *
     * @param key Key
     * @param value Value
     */
    public ReplaceEntry(String key, String value) {
        super(key, value);
    }
}
