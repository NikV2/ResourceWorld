package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
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
        this.resetTeleport = new ResetTeleport(plugin);
        this.worldGeneratorEnd = new WorldGeneratorEnd(plugin);
        this.worldCommands = new WorldCommands(plugin);
        this.teleport = new Teleport(plugin);
    }

    @Override
    public void run() {
        if (!WorldUtils.endExists()) return;
        teleport.setResettingEnd(true);
        if (plugin.getConfig().getBoolean("end_world.settings.automated_resets.store_time_on_shutdown")) {
            plugin.getData().set("end.millis", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_END.getMessage());
        resetTeleport.resetEndTP();
        World world = Bukkit.getWorld(plugin.getConfig().getString("end_world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    WorldUtils.deleteDirectory(world.getWorldFolder());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                worldGeneratorEnd.createWorld();
                worldCommands.endRunCommands();
                plugin.getServer().broadcastMessage(MsgType.END_HAS_BEEN_RESET.getMessage());
                teleport.setResettingEnd(false);
            }
        }.runTaskLater(plugin, 90);
    }
}