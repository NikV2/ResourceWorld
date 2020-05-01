package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetNetherWorld extends BukkitRunnable {
    private final ResourceWorld plugin = ResourceWorld.getInstance();
    private final WorldUtils worldUtils = new WorldUtils();

    @Override
    public void run() {
        if (!worldUtils.netherExists()) return;
        if (Config.get().getBoolean("nether_world.settings.automated_resets.store_time_on_shutdown")) {
            Data.get().set("nether.millis", System.currentTimeMillis());
            Data.save();
            Data.reload();
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
                } catch (NullPointerException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGeneratorNether(plugin).createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("nether_has_been_reset"));
            }
        }.runTaskLater(plugin, 90);
    }
}
