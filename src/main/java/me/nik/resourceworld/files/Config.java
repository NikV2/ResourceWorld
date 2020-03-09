package me.nik.resourceworld.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
public class Config {
    private static File file;
    private static FileConfiguration config;
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ResourceWorld").getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                //Does not exist
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            //Cannot save file
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void addDefaults() {
        //config.yml
        Config.get().addDefault("Enabled", false);
        Config.get().addDefault("World Name", "Resource");
        Config.get().addDefault("Generate Structures", true);
        Config.get().addDefault("World Type", "NORMAL");
        Config.get().addDefault("Use Custom Seed", false);
        Config.get().addDefault("Seed", -686298914);
    }
}
