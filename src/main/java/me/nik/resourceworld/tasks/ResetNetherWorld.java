package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetNetherWorld extends BukkitRunnable {

    private final ResourceWorld plugin;
    private final WorldUtils worldUtils = new WorldUtils();

    public ResetNetherWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!worldUtils.netherExists()) return;
        if (Config.get().getBoolean("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("nether.millis", System.currentTimeMillis());
            plugin.data.save();
            plugin.data.reload();
        }
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_nether"));
        new ResetTeleport().resetNetherTP();
        World world = Bukkit.getWorld(Config.get().getString("nether_world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    worldUtils.deleteDirectory(world.getWorldFolder());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGeneratorNether().createWorld();
                new WorldCommands().netherRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message("nether_has_been_reset"));
            }
        }.runTaskLater(plugin, 90);
    }
}
