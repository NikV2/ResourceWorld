package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.discord.Discord;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.TaskUtils;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;

public class ResetByCommand {

    private final ResourceWorld plugin;

    private final ResetTeleport resetTeleport;
    private final WorldGenerator worldGenerator;
    private final WorldGeneratorNether worldGeneratorNether;
    private final WorldGeneratorEnd worldGeneratorEnd;
    private final WorldCommands worldCommands;
    private final Teleport teleport;

    public ResetByCommand(ResourceWorld plugin) {
        this.plugin = plugin;
        this.resetTeleport = new ResetTeleport();
        this.worldGenerator = new WorldGenerator();
        this.worldGeneratorNether = new WorldGeneratorNether();
        this.worldGeneratorEnd = new WorldGeneratorEnd();
        this.worldCommands = new WorldCommands();
        this.teleport = new Teleport(plugin);
    }

    public void executeReset() {
        if (!WorldUtils.worldExists()) return;
        teleport.setResettingWorld(true);
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_WORLD.getMessage());
        resetTeleport.resetTP();
        Messenger.consoleMessage(MsgType.DELETING.getMessage());
        World world = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        TaskUtils.taskAsync(() -> {
            try {
                WorldUtils.deleteDirectory(world.getWorldFolder());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
        TaskUtils.taskLater(() -> {
            worldGenerator.createWorld();
            worldCommands.worldRunCommands();
            plugin.getServer().broadcastMessage(MsgType.WORLD_HAS_BEEN_RESET.getMessage());
            teleport.setResettingWorld(false);
            plugin.getData().set("world.papi", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }, 80);
        if (Config.Setting.SETTINGS_DISCORD_WORLD.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource World has been Reset!", Color.GREEN);
            discord.sendNotification();
        }
    }

    public void executeNetherReset() {
        if (!WorldUtils.netherExists()) return;
        teleport.setResettingNether(true);
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_NETHER.getMessage());
        resetTeleport.resetNetherTP();
        Messenger.consoleMessage(MsgType.DELETING.getMessage());
        World world = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        TaskUtils.taskAsync(() -> {

            try {
                WorldUtils.deleteDirectory(world.getWorldFolder());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
        TaskUtils.taskLater(() -> {
            worldGeneratorNether.createWorld();
            worldCommands.netherRunCommands();
            plugin.getServer().broadcastMessage(MsgType.NETHER_HAS_BEEN_RESET.getMessage());
            teleport.setResettingNether(false);
            plugin.getData().set("nether.papi", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }, 80);
        if (Config.Setting.SETTINGS_DISCORD_NETHER.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource Nether has been Reset!", Color.RED);
            discord.sendNotification();
        }
    }

    public void executeEndReset() {
        if (!WorldUtils.endExists()) return;
        teleport.setResettingEnd(true);
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_END.getMessage());
        resetTeleport.resetEndTP();
        Messenger.consoleMessage(MsgType.DELETING.getMessage());
        World world = Bukkit.getWorld(Config.Setting.END_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        TaskUtils.taskAsync(() -> {

            try {
                WorldUtils.deleteDirectory(world.getWorldFolder());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
        TaskUtils.taskLater(() -> {
            worldGeneratorEnd.createWorld();
            worldCommands.endRunCommands();
            plugin.getServer().broadcastMessage(MsgType.END_HAS_BEEN_RESET.getMessage());
            teleport.setResettingEnd(false);
            plugin.getData().set("end.papi", System.currentTimeMillis());
            plugin.saveData();
            plugin.reloadData();
        }, 80);
        if (Config.Setting.SETTINGS_DISCORD_END.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource End has been Reset!", Color.BLUE);
            discord.sendNotification();
        }
    }
}