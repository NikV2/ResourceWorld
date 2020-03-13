package me.nik.resourceworld.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
public class Lang {
    private static File file;
    private static FileConfiguration config;
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ResourceWorld").getDataFolder(), "lang.yml");
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
        //lang.yml
        Lang.get().addDefault("Prefix", "&a&l[&2Resource World&a&l]&f&l: ");
        Lang.get().addDefault("Not Enabled", "&f&lResource World is Disabled, Enable it from the &f&l&nconfig.yml");
        Lang.get().addDefault("Generating", "&f&lGenerating a Resource World!");
        Lang.get().addDefault("Deleting", "&f&lCleaning up the old Resource World");
        Lang.get().addDefault("Generated", "&f&lA New Resource World has been Generated!");
        Lang.get().addDefault("Automated Resets Disabled", "&f&lAutomated Resets are Disabled, Skipping");
        Lang.get().addDefault("Automated Resets Enabled", "&f&lAutomated Resets are Enabled, Starting tasks");
        Lang.get().addDefault("Resetting The World", "&fCleaning up the Resource World, This may cause Lag!");
        Lang.get().addDefault("World Has Been Reset", "&fThe Resource World has been Reset!");
        Lang.get().addDefault("Unsafe Location", "&fDid not find a safe location to teleport, Please try again!");
        Lang.get().addDefault("Console Message", "&f&lYou cannot execute Resource World commands through the console:(");
        Lang.get().addDefault("No Perm", "&cYou do not have permission to execute this command!");
        Lang.get().addDefault("Cooldown Message", "&cYou must before Randomly teleporting to the Resource World again!");
        Lang.get().addDefault("Reloaded", "&fYou have successfully reloaded the plugin!");
        Lang.get().addDefault("Reloading", "&fReloading... This may cause lag!");
        Lang.get().addDefault("DisabledCommand", "&cSorry, You can't use that Command in this World!");
    }
}
