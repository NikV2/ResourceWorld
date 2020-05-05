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
            } catch (IOException ignored) {
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
        } catch (IOException ignored) {
        }
    }

    public static void reload() {
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public static void addDefaults() {
        //lang.yml
        Lang.get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        Lang.get().addDefault("prefix", "&a&l[&2Resource World&a&l]&f&l: ");
        Lang.get().addDefault("update_found", "&aThere is an Update available, Your version &f%current%&a new version &f%new%");
        Lang.get().addDefault("disabled_command", "&cSorry, You can't use that Command in this World!");
        Lang.get().addDefault("deleting", "&f&lCleaning up the old Resource World");
        Lang.get().addDefault("reset_cooldown", "&cWARNING! You cannot reset the Resource World yet, You must wait %seconds% seconds.");
        Lang.get().addDefault("automated_resets_disabled", "&f&lAutomated Resets are Disabled, Skipping.");
        Lang.get().addDefault("automated_resets_enabled", "&f&lAutomated Resets are Enabled, Initializing.");
        Lang.get().addDefault("resetting_the_world", "&fCleaning up the Resource World, This may cause Lag!");
        Lang.get().addDefault("resetting_the_nether", "&fCleaning up the Nether World, This may cause Lag!");
        Lang.get().addDefault("resetting_the_end", "&fCleaning up the End World, This may cause Lag!");
        Lang.get().addDefault("world_has_been_reset", "&fThe Resource World has been Reset!");
        Lang.get().addDefault("nether_has_been_reset", "&fThe Nether World has been Reset!");
        Lang.get().addDefault("end_has_been_reset", "&fThe End World has been Reset!");
        Lang.get().addDefault("console_message", "&f&lThis command cannot be executed through the Console :(");
        Lang.get().addDefault("no_perm", "&cYou do not have permission to execute this command!");
        Lang.get().addDefault("cooldown_message", "&cYou can teleport to the Resource World again in %seconds% seconds.");
        Lang.get().addDefault("block_place", "&cSorry but you cannot place this Block in this World.");
        Lang.get().addDefault("reloaded", "&fYou have successfully reloaded the plugin!");
        Lang.get().addDefault("reloading", "&fReloading... This may cause lag!");
        Lang.get().addDefault("teleport_delay", "&aTeleporting to the Resource World in %seconds% seconds.");
        Lang.get().addDefault("gui_name", "&2&lResource World Menu");
        Lang.get().addDefault("settings_gui_name", "&2&lResource World Settings");
        Lang.get().addDefault("worlds_gui_name", "&2&lReset A Resource World");
        Lang.get().addDefault("teleported_message", "&fYou have been Teleported back to spawn!");
        Lang.get().addDefault("not_exist", "&cThe Resource World is currently under Maintenance, Please try again later!");
        Lang.get().addDefault("teleported_players", "&aTeleported all the players back to spawn!");
        Lang.get().addDefault("main_world_error", "&cWe could not teleport you back to the main world, Please contact an Administrator.");
        Lang.get().addDefault("update_not_found", "&f&lYou're running the Latest Version &c&l<3");
        Lang.get().addDefault("update_disabled", "&f&lUpdate Checker is Disabled, Skipping");
    }
}
