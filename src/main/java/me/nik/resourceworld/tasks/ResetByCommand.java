package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetByCommand {

    private final ResourceWorld plugin;

    private final String overworld;
    private final String nether;
    private final String end;

    public ResetByCommand(ResourceWorld plugin) {
        this.plugin = plugin;
        this.overworld = plugin.getConfig().getString("world.settings.world_name");
        this.nether = plugin.getConfig().getString("nether_world.settings.world_name");
        this.end = plugin.getConfig().getString("end_world.settings.world_name");
    }

    public void executeReset() {
        if (!WorldUtils.worldExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_WORLD));
        new ResetTeleport(plugin).resetTP();
        Messenger.consoleMessage(Messenger.message(MsgType.DELETING));
        World world = Bukkit.getWorld(overworld);
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
        }.runTaskLater(plugin, 80);
    }

    public void executeNetherReset() {
        if (!WorldUtils.netherExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_NETHER));
        new ResetTeleport(plugin).resetNetherTP();
        Messenger.consoleMessage(Messenger.message(MsgType.DELETING));
        World world = Bukkit.getWorld(nether);
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
        }.runTaskLater(plugin, 80);
    }

    public void executeEndReset() {
        if (!WorldUtils.endExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_END));
        new ResetTeleport(plugin).resetEndTP();
        Messenger.consoleMessage(Messenger.message(MsgType.DELETING));
        World world = Bukkit.getWorld(end);
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
                new WorldGeneratorEnd(plugin).createWorld();
                new WorldCommands(plugin).endRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.END_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 80);
    }
}
