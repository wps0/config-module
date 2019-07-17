package pl.wieczorekp.configmodule;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import pl.wieczorekp.configmodule.config.Config;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public interface IConfigurableJavaPlugin {
    @Nullable
    static IConfigurableJavaPlugin getInstance(@NotNull String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends JavaPlugin>> subTypes = reflections.getSubTypesOf(JavaPlugin.class);
        Iterator<Class<? extends JavaPlugin>> iterator = subTypes.iterator();

        try {
            if (iterator.hasNext()) {
                Class<? extends JavaPlugin> targetClass = iterator.next();
                Method instanceMethod = targetClass.getDeclaredMethod("getInstance");

                System.out.println("metoda: " + instanceMethod.toString());
                return (IConfigurableJavaPlugin) instanceMethod.invoke(targetClass);
            }

        } catch (Exception e) {
            System.out.println("Internal error!");
            e.printStackTrace();
        }

        System.out.println("null!");
        return null;
    }

    Config getConfigService();

    ///////////////////////////////////////////////////////////////////////////
    // Override some of the JavaPlugin methods
    ///////////////////////////////////////////////////////////////////////////
    String getName();

    File getDataFolder();

    FileConfiguration getConfig();

    InputStream getResource(String filename);

    void saveResource(String resourcePath, boolean replace);

    Logger getLogger();

    void saveDefaultConfig();
}
