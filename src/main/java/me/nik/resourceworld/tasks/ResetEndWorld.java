package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
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
        if (Config.get().getBoolean("end_world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("end.millis", System.currentTimeMillis());
            plugin.data.save();
            plugin.data.reload();
        }
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_end"));
        new ResetTeleport().resetEndTP();
        World world = Bukkit.getWorld(Config.get().getString("end_world.settings.world_name"));
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
                new WorldGenerator().createWorld();
                new WorldCommands().endRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message("end_has_been_reset"));
            }
        }.runTaskLater(plugin, 90);
    }
}
