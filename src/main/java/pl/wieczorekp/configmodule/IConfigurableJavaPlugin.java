package pl.wieczorekp.configmodule;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public interface IConfigurableJavaPlugin {
    static IConfigurableJavaPlugin getInstance() {
        return IConfigurableJavaPlugin.getInstance("pl.wieczorekp.wmap");
    }

    /*static IConfigurableJavaPlugin getInstance(Method instanceMethod) {
        return instanceMethod.invoke();
    }*/

    static IConfigurableJavaPlugin getInstance(String packageName) {
            Reflections reflections = new Reflections(packageName);

            Set<Class<? extends JavaPlugin>> subTypes = reflections.getSubTypesOf(JavaPlugin.class);
            Iterator<Class<? extends JavaPlugin>> iterator = subTypes.iterator();

            System.out.println(subTypes.size());

        try {
            if (iterator.hasNext()) {
                Class<? extends JavaPlugin> targetClass = iterator.next();
//                System.out.println("klasa: " + targetClass.getName());
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
    // Override some of the JavaPlugin methods
    String getName();

    File getDataFolder();

    FileConfiguration getConfig();

    InputStream getResource(String filename);

    void saveResource(String resourcePath, boolean replace);

    Logger getLogger();

    void saveDefaultConfig();
}
