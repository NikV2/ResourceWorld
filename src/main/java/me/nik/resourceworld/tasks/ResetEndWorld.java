package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.discord.Discord;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetEndWorld extends BukkitRunnable {

    private final ResourceWorld plugin;
    private final ResetTeleport resetTeleport;
    private final WorldGeneratorEnd worldGeneratorEnd;
    private final WorldCommands worldCommands;
    private final Teleport teleport;

    public ResetEndWorld(ResourceWorld plugin) {
        this.plugin = plugin;
        this.resetTeleport = new ResetTeleport();
        this.worldGeneratorEnd = new WorldGeneratorEnd();
        this.worldCommands = new WorldCommands();
        this.teleport = new Teleport();
    }

    @Override
    public void run() {
        if (!WorldUtils.endExists()) return;
        teleport.setResettingEnd(true);
        if (Config.Setting.END_STORE_TIME.getBoolean()) {
            plugin.getData().set("end.millis", System.currentTimeMillis());
        }
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_END.getMessage());
        resetTeleport.resetEndTP();
        World world = Bukkit.getWorld(Config.Setting.END_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        try {
            WorldUtils.deleteDirectory(world.getWorldFolder());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        worldGeneratorEnd.createWorld();
        worldCommands.endRunCommands();
        plugin.getServer().broadcastMessage(MsgType.END_HAS_BEEN_RESET.getMessage());
        teleport.setResettingEnd(false);
        plugin.getData().set("end.papi", System.currentTimeMillis());
        plugin.saveData();
        plugin.reloadData();
        if (Config.Setting.SETTINGS_DISCORD_END.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource End has been Reset!", Color.BLUE);
            discord.sendNotification();
        }
    }
}