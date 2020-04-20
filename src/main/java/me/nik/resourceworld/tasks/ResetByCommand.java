package me.nik.resourceworld.tasks;

import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetByCommand extends Manager {

    public void executeReset() {
        if (!worldExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetTP();
        System.out.println(Messenger.message("deleting"));
        World world = Bukkit.getWorld(configString("world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    deleteDirectory(world.getWorldFolder());
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

    public void executeNetherReset() {
        if (!netherExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetNetherTP();
        System.out.println(Messenger.message("deleting"));
        World world = Bukkit.getWorld(configString("nether_world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    deleteDirectory(world.getWorldFolder());
                } catch (NullPointerException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGeneratorNether().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }

    public void executeEndReset() {
        if (!endExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetEndTP();
        System.out.println(Messenger.message("deleting"));
        World world = Bukkit.getWorld(configString("end_world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    deleteDirectory(world.getWorldFolder());
                } catch (NullPointerException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
                }
            }
        }.runTaskAsynchronously(plugin);
        new BukkitRunnable() {

            @Override
            public void run() {
                new WorldGeneratorEnd().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }
}
