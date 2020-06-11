package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;

import java.util.List;

public class WorldCommands {

    private final List<String> wCmds;
    private final List<String> nCmds;
    private final List<String> eCmds;
    private final boolean wRunCommands;
    private final boolean nRunCommands;
    private final boolean eRunCommands;

    public WorldCommands(ResourceWorld plugin) {
        this.wCmds = plugin.getConfig().getStringList("world.settings.commands_after_reset.commands");
        this.nCmds = plugin.getConfig().getStringList("nether_world.settings.commands_after_reset.commands");
        this.eCmds = plugin.getConfig().getStringList("end_world.settings.commands_after_reset.commands");
        this.wRunCommands = plugin.getConfig().getBoolean("world.settings.commands_after_reset.enabled");
        this.nRunCommands = plugin.getConfig().getBoolean("nether_world.settings.commands_after_reset.enabled");
        this.eRunCommands = plugin.getConfig().getBoolean("end_world.settings.commands_after_reset.enabled");
    }

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
