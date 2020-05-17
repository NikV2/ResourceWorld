package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

import java.util.List;

public class WorldCommands {

    private final List<String> wCmds = Config.get().getStringList("world.settings.commands_after_reset.commands");
    private final List<String> nCmds = Config.get().getStringList("nether_world.settings.commands_after_reset.commands");
    private final List<String> eCmds = Config.get().getStringList("end_world.settings.commands_after_reset.commands");
    private final boolean wRunCommands = Config.get().getBoolean("world.settings.commands_after_reset.enabled");
    private final boolean nRunCommands = Config.get().getBoolean("nether_world.settings.commands_after_reset.enabled");
    private final boolean eRunCommands = Config.get().getBoolean("end_world.settings.commands_after_reset.enabled");

    public void worldRunCommands() {
        if (!wRunCommands) return;
        for (String cmd : wCmds) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void netherRunCommands() {
        if (!nRunCommands) return;
        for (String cmd : nCmds) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void endRunCommands() {
        if (!eRunCommands) return;
        for (String cmd : eCmds) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }
}
