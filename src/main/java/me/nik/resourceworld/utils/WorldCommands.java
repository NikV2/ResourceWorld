package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

public class WorldCommands {

    public void worldRunCommands() {
        if (!Config.Setting.WORLD_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.WORLD_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void netherRunCommands() {
        if (!Config.Setting.NETHER_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.NETHER_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void endRunCommands() {
        if (!Config.Setting.END_COMMANDS_ENABLED.getBoolean()) return;
        for (String cmd : Config.Setting.END_COMMANDS_COMMANDS.getStringList()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }
}