package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Data;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetNetherWorld extends BukkitRunnable {
    private final ResourceWorld plugin = ResourceWorld.getInstance();
    private final Manager manager = new Manager();

    @Override
    public void run() {
        if (!manager.netherExists()) return;
        Data.get().set("nether.millis", System.currentTimeMillis());
        Data.save();
        Data.reload();
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_nether"));
        new ResetTeleport().resetNetherTP();
        World world = Bukkit.getWorld(Config.get().getString("nether_world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    manager.deleteDirectory(world.getWorldFolder());
                } catch (NullPointerException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGeneratorNether().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("nether_has_been_reset"));
            }
        }.runTaskLater(plugin, 90);
    }
}
