package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetWorld extends BukkitRunnable {

    private final ResourceWorld plugin;
    private final ResetTeleport resetTeleport;
    private final WorldGenerator worldGenerator;
    private final WorldCommands worldCommands;

    private final Teleport teleport;

    public ResetWorld(ResourceWorld plugin) {
        this.plugin = plugin;
        this.resetTeleport = new ResetTeleport(plugin);
        this.worldGenerator = new WorldGenerator(plugin);
        this.worldCommands = new WorldCommands(plugin);
        this.teleport = new Teleport(plugin);
    }

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        teleport.setResettingWorld(true);
        if (plugin.getConfig().getBoolean("world.settings.automated_resets.store_time_on_shutdown")) {
            plugin.getData().set("world.millis", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_WORLD.getMessage());
        resetTeleport.resetTP();
        World world = Bukkit.getWorld(plugin.getConfig().getString("world.settings.world_name"));
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
                worldGenerator.createWorld();
                worldCommands.worldRunCommands();
                plugin.getServer().broadcastMessage(MsgType.WORLD_HAS_BEEN_RESET.getMessage());
                teleport.setResettingWorld(false);
            }
        }.runTaskLater(plugin, 90);
    }
}