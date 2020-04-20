package me.nik.resourceworld;

import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.*;
import me.nik.resourceworld.tasks.ResetEndWorld;
import me.nik.resourceworld.tasks.ResetNetherWorld;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldGeneratorEnd;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ResourceWorld extends JavaPlugin {

    private static ResourceWorld instance;

    @Override
    public void onEnable() {
        instance = this;
        //Load Files
        loadFiles();

        //Startup Message
        System.out.println();
        System.out.println("            " + ChatColor.GREEN + "Resource World " + ChatColor.UNDERLINE + "v" + this.getDescription().getVersion());
        System.out.println();
        System.out.println("                   " + ChatColor.WHITE + "Author: " + ChatColor.UNDERLINE + "Nik");
        System.out.println();
        System.out.println("     " + ChatColor.GREEN + "Running on " + ChatColor.WHITE + getServer().getVersion());
        System.out.println();

        //Load Commands
        getCommand("resource").setExecutor(new CommandManager());

        initialize();

        generateWorlds();

        startIntervals();

        //Check for updates
        if (Config.get().getBoolean("settings.check_for_updates")) {
            BukkitTask updateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            System.out.println(Messenger.message("update_disabled"));
        }

        //Enable bStats
        int pluginId = 6981;
        MetricsLite metricsLite = new MetricsLite(this, pluginId);
    }

    @Override
    public void onDisable() {

        //Cancel all tasks
        Bukkit.getScheduler().cancelTasks(this);

        //Reload Files
        Config.reload();
        Config.save();
        Lang.reload();
        Lang.save();

        //Unload Instance
        instance = null;
    }

    public static ResourceWorld getInstance() {
        return instance;
    }

    private void loadFiles() {
        Config.setup();
        Config.addDefaults();
        Config.get().options().copyDefaults(true);
        Config.save();
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();
    }

    private void initialize() {
        registerEvent(new LeaveInWorld());
        registerEvent(new MenuHandler());
        if (Config.get().getBoolean("world.settings.block_regeneration.enabled")) {
            registerEvent(new BlockRegen());
        }
        if (Config.get().getBoolean("world.settings.disable_suffocation_damage") || Config.get().getBoolean("world.settings.disable_drowning_damage") || Config.get().getBoolean("world.settings.disable_entity_spawning")) {
            registerEvent(new WorldSettings());
        }
        if (Config.get().getBoolean("disabled_commands.enabled")) {
            registerEvent(new DisabledCmds());
        }
    }

    private void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void startIntervals() {
        if (Config.get().getBoolean("world.settings.automated_resets.enabled")) {
            System.out.println(Messenger.message("automated_resets_enabled"));
            int interval = Config.get().getInt("world.settings.automated_resets.interval") * 72000;
            BukkitTask resetWorld = new ResetWorld().runTaskTimer(this, interval, interval);
        }
        if (Config.get().getBoolean("nether_world.settings.enabled") && Config.get().getBoolean("nether_world.settings.automated_resets.enabled")) {
            int interval = Config.get().getInt("nether_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetNether = new ResetNetherWorld().runTaskTimer(this, interval, interval);
        }
        if (Config.get().getBoolean("end_world.settings.enabled") && Config.get().getBoolean("end_world.settings.automated_resets.enabled")) {
            int interval = Config.get().getInt("end_world.settings.automated_resets.interval") * 72000;
            BukkitTask resetEnd = new ResetEndWorld().runTaskTimer(this, interval, interval);
        }
    }

    private void generateWorlds() {
        new WorldGenerator().createWorld();
        if (Config.get().getBoolean("nether_world.settings.enabled")) {
            new WorldGeneratorNether().createWorld();
        }
        if (Config.get().getBoolean("end_world.settings.enabled")) {
            new WorldGeneratorEnd().createWorld();
        }
    }
}