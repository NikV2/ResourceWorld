package me.nik.resourceworld.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Lang {
    private File file;
    private FileConfiguration lang;

    public void setup(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "lang.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return lang;
    }

    public void save() {
        try {
            lang.save(file);
        } catch (IOException ignored) {
        }
    }

    public void reload() {
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public void addDefaults() {
        //lang.yml
        get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                         Resource World                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        get().addDefault("prefix", "&a&l[&2Resource World&a&l]&f&l: ");
        get().addDefault("update_found", "&aThere is an Update available, Your version &f%current%&a new version &f%new%");
        get().addDefault("disabled_command", "&cSorry, You can't use that Command in this World!");
        get().addDefault("deleting", "&f&lCleaning up the old Resource World");
        get().addDefault("reset_cooldown", "&cWARNING! You cannot reset the Resource World yet, You must wait %seconds% seconds.");
        get().addDefault("resetting_the_world", "&fCleaning up the Resource World, This may cause Lag!");
        get().addDefault("resetting_the_nether", "&fCleaning up the Nether World, This may cause Lag!");
        get().addDefault("resetting_the_end", "&fCleaning up the End World, This may cause Lag!");
        get().addDefault("world_has_been_reset", "&fThe Resource World has been Reset!");
        get().addDefault("nether_has_been_reset", "&fThe Nether World has been Reset!");
        get().addDefault("end_has_been_reset", "&fThe End World has been Reset!");
        get().addDefault("console_message", "&f&lThis command cannot be executed through the Console :(");
        get().addDefault("no_perm", "&cYou do not have permission to execute this command!");
        get().addDefault("cooldown_message", "&cYou can teleport to the Resource World again in %seconds% seconds.");
        get().addDefault("cooldown_spawn", "&cYou can teleport to spawn again in %seconds% seconds.");
        get().addDefault("reloaded", "&fYou have successfully reloaded the plugin!");
        get().addDefault("teleport_delay", "&aTeleporting to the Resource World in %seconds% seconds.");
        get().addDefault("gui_name", "&2&lResource World Menu");
        get().addDefault("worlds_gui_name", "&2&lReset A Resource World");
        get().addDefault("teleported_message", "&fYou have been Teleported back to spawn!");
        get().addDefault("not_exist", "&cThe Resource World is currently under Maintenance, Please try again later!");
        get().addDefault("teleport_paid", "&a%price% &fHave been taken from your balance.");
        get().addDefault("teleport_error", "&cYou do not have enough money to Teleport to the Resource World.");
        get().addDefault("teleported_players", "&aTeleported all the players back to spawn!");
        get().addDefault("teleporting_player", "&aTeleporting %player% to %world%.");
        get().addDefault("main_world_error", "&cWe could not teleport you back to the main world, Please contact an Administrator.");
        get().addDefault("update_not_found", "&f&lYou're running the Latest Version &c&l<3");
        get().addDefault("update_disabled", "&f&lUpdate Checker is Disabled, Skipping");
    }
}