package me.nik.resourceworld.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang {
    private static File file;
    private static FileConfiguration lang;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ResourceWorld").getDataFolder(), "lang.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                //Does not exist
            }
        }
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return lang;
    }

    public static void save() {
        try {
            lang.save(file);
        } catch (IOException e) {
            //Cannot save file
        }
    }

    public static void reload() {
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public static void addDefaults() {
        //lang.yml
        Lang.get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        Lang.get().addDefault("prefix", "&a&l[&2Resource World&a&l]&f&l: ");
        Lang.get().addDefault("not_enabled", "&f&lResource World is Disabled, Enable it from the &f&l&nconfig.yml&f&l Then use /Resource Reload.");
        Lang.get().addDefault("generating", "&f&lGenerating a Resource World!");
        Lang.get().addDefault("deleting", "&f&lCleaning up the old Resource World");
        Lang.get().addDefault("reset_cooldown", "&cWARNING! You cannot reset the Resource World yet, You must wait ");
        Lang.get().addDefault("generated", "&f&lA New Resource World has been Generated!");
        Lang.get().addDefault("automated_resets_disabled", "&f&lAutomated Resets are Disabled, Skipping");
        Lang.get().addDefault("automated_resets_enabled", "&f&lAutomated Resets are Enabled, Starting tasks");
        Lang.get().addDefault("resetting_the_world", "&fCleaning up the Resource World, This may cause Lag!");
        Lang.get().addDefault("world_has_been_reset", "&fThe Resource World has been Reset!");
        Lang.get().addDefault("unsafe_location", "&fDid not find a safe location to teleport, Please try again!");
        Lang.get().addDefault("console_message", "&f&lThis command cannot be executed through the Console :(");
        Lang.get().addDefault("no_perm", "&cYou do not have permission to execute this command!");
        Lang.get().addDefault("cooldown_message", "&cYou can teleport to the Resource World again in ");
        Lang.get().addDefault("reloaded", "&fYou have successfully reloaded the plugin!");
        Lang.get().addDefault("reloading", "&fReloading... This may cause lag!");
        Lang.get().addDefault("disabled_command", "&cSorry, You can't use that Command in this World!");
        Lang.get().addDefault("teleport_delay", "&aTeleporting to the Resource World in ");
        Lang.get().addDefault("gui_name", "&2&lResource World Menu");
        Lang.get().addDefault("teleported_message", "&fYou have been Teleported back to spawn!");
        Lang.get().addDefault("not_exist", "&cThe Resource World is currently under Maintenance, Please try again later!");
        Lang.get().addDefault("teleported_players", "&aTeleported all the players back to spawn!");
        Lang.get().addDefault("update_found", "&f&lThere is a new version available on Spigot!");
        Lang.get().addDefault("update_not_found", "&f&lYou're running the Latest Version &c&l<3");
        Lang.get().addDefault("update_disabled", "&f&lUpdate Checker is Disabled, Skipping");
    }
}
