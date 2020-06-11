package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
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

    public ResetNetherWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!WorldUtils.netherExists()) return;
        if (plugin.getConfig().getBoolean("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            plugin.getData().set("nether.millis", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_NETHER));
        new ResetTeleport(plugin).resetNetherTP();
        World world = Bukkit.getWorld(plugin.getConfig().getString("nether_world.settings.world_name"));
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
                new WorldGeneratorNether(plugin).createWorld();
                new WorldCommands(plugin).netherRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.NETHER_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 90);
    }
}
