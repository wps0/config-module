package pl.wieczorekp.configmodule.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {
    private static final HashMap<String, Pattern> patternCache = new HashMap<>();

    /**
     * Resolves absolute path of given configFile to the relative one.
     *
     * @param configFile Config file to which path should be returned.
     * @return Relative path to the yml file.
     */
    @Nullable
    public static String getFilePathFromConfig(@NotNull File configFile, String pluginName) {
        if (patternCache.get(pluginName) == null)
            patternCache.put(pluginName, Pattern.compile(pluginName + "([/\\\\])(.+)"));

        Matcher matcher = patternCache.get(pluginName).matcher(configFile.getPath());
        if (!matcher.find())
            return null;

        return matcher.group(2);
    }
}
