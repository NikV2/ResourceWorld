package me.nik.resourceworld.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Data {
    private File file;
    private static FileConfiguration data;

    public void setup(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return data;
    }

    public void save() {
        try {
            data.save(file);
        } catch (IOException ignored) {
        }
    }

    public void reload() {
        data = YamlConfiguration.loadConfiguration(file);
    }

    public void addDefaults() {
        //lang.yml
        Data.get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        Data.get().addDefault("world.timer", 0);
        Data.get().addDefault("world.millis", 0);
        Data.get().addDefault("nether.timer", 0);
        Data.get().addDefault("nether.millis", 0);
        Data.get().addDefault("end.timer", 0);
        Data.get().addDefault("end.millis", 0);
    }
}
