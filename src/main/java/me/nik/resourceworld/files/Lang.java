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
        Lang.get().addDefault("prefix", "&a&l[&2Resource World&a&l]&f&l: ");
        Lang.get().addDefault("not_enabled", "&f&lResource World is Disabled, Enable it from the &f&l&nconfig.yml&f&l Then use /Resource Reload.");
        Lang.get().addDefault("generating", "&f&lGenerating a Resource World!");
        Lang.get().addDefault("deleting", "&f&lCleaning up the old Resource World");
        Lang.get().addDefault("generated", "&f&lA New Resource World has been Generated!");
        Lang.get().addDefault("automated_resets_disabled", "&f&lAutomated Resets are Disabled, Skipping");
        Lang.get().addDefault("automated_resets_enabled", "&f&lAutomated Resets are Enabled, Starting tasks");
        Lang.get().addDefault("resetting_the_world", "&fCleaning up the Resource World, This may cause Lag!");
        Lang.get().addDefault("world_has_been_reset", "&fThe Resource World has been Reset!");
        Lang.get().addDefault("unsafe_location", "&fDid not find a safe location to teleport, Please try again!");
        Lang.get().addDefault("console_message", "&f&lYou cannot execute Resource World commands through the console:(");
        Lang.get().addDefault("no_perm", "&cYou do not have permission to execute this command!");
        Lang.get().addDefault("cooldown_message", "&cYou can teleport to the Resource World again in ");
        Lang.get().addDefault("reloaded", "&fYou have successfully reloaded the plugin!");
        Lang.get().addDefault("reloading", "&fReloading... This may cause lag!");
        Lang.get().addDefault("disabled_command", "&cSorry, You can't use that Command in this World!");
        Lang.get().addDefault("teleport_delay", "&aTeleporting to the Resource World in ");
        Lang.get().addDefault("gui_name", "&2&lResource World Menu");
        Lang.get().addDefault("not_exist", "&cThe Resource World is currently under Maintenance, Please try again later!");
    }
}
