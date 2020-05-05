package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetByCommand {

    private final ResourceWorld plugin;
    private final WorldUtils worldUtils = new WorldUtils();

    private final String overworld = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");

    public ResetByCommand(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    public void executeReset() {
        if (!worldUtils.worldExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetTP();
        plugin.consoleMessage(Messenger.message("deleting"));
        World world = Bukkit.getWorld(overworld);
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
                new WorldGenerator().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }

    public void executeNetherReset() {
        if (!worldUtils.netherExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetNetherTP();
        plugin.consoleMessage(Messenger.message("deleting"));
        World world = Bukkit.getWorld(nether);
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
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }

    public void executeEndReset() {
        if (!worldUtils.endExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message("resetting_the_world"));
        new ResetTeleport().resetEndTP();
        plugin.consoleMessage(Messenger.message("deleting"));
        World world = Bukkit.getWorld(end);
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
                new WorldGeneratorEnd().createWorld();
                plugin.getServer().broadcastMessage(Messenger.message("world_has_been_reset"));
            }
        }.runTaskLater(plugin, 80);
    }
}
