package pl.wieczorekp.configmodule;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "WConfigModule", version = "1.1.5-SNAPSHOT")
public class WConfigModule extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("OnEnable - WConfigModule");
    }

    public static void main(String[] args) {
        System.out.println("ERROR: This JAR is a Minecraft Spigot Plugin (https://www.spigotmc.org/) thus cannot be run as a separate app");
    }
}
