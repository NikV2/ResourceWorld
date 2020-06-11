package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetWorld extends BukkitRunnable {

    private final ResourceWorld plugin;

    public ResetWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        if (plugin.getConfig().getBoolean("world.settings.automated_resets.store_time_on_shutdown")) {
            plugin.getData().set("world.millis", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_WORLD));
        new ResetTeleport(plugin).resetTP();
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
                new WorldGenerator(plugin).createWorld();
                new WorldCommands(plugin).worldRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.WORLD_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 90);
    }
}
