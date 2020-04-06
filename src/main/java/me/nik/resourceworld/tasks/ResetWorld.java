package me.nik.resourceworld.tasks;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class ResetWorld extends BukkitRunnable {
    ResourceWorld plugin;
    public ResetWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetTP();
        World world = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    FileUtils.deleteQuietly(new File(world.getName()));
                } catch (NullPointerException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGenerator().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }
}
