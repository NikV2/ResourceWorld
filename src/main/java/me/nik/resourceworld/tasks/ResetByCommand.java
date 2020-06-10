package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
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

    private final String overworld = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");

    public ResetByCommand(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    public void executeReset() {
        if (!WorldUtils.worldExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_WORLD));
        new ResetTeleport().resetTP();
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
                new WorldGenerator().createWorld();
                new WorldCommands().worldRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.WORLD_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 80);
    }

    public void executeNetherReset() {
        if (!WorldUtils.netherExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_NETHER));
        new ResetTeleport().resetNetherTP();
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
                new WorldGeneratorNether().createWorld();
                new WorldCommands().netherRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.NETHER_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 80);
    }

    public void executeEndReset() {
        if (!WorldUtils.endExists()) return;
        plugin.getServer().broadcastMessage(Messenger.message(MsgType.RESETTING_THE_END));
        new ResetTeleport().resetEndTP();
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
                new WorldGeneratorEnd().createWorld();
                new WorldCommands().endRunCommands();
                plugin.getServer().broadcastMessage(Messenger.message(MsgType.END_HAS_BEEN_RESET));
            }
        }.runTaskLater(plugin, 80);
    }
}
