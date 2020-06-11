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

public class ResetEndWorld extends BukkitRunnable {

    private final ResourceWorld plugin;

    public ResetEndWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!WorldUtils.endExists()) return;
        if (plugin.getConfig().getBoolean("end_world.settings.automated_resets.store_time_on_shutdown")) {
            plugin.getData().set("end.millis", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_END));
        new ResetTeleport(plugin).resetEndTP();
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
                new WorldGenerator(plugin).createWorld();
                new WorldCommands(plugin).endRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.END_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 90);
    }
}
