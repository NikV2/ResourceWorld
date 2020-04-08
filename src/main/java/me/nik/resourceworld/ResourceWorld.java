package me.nik.resourceworld;

import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.MenuHandler;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.*;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ResourceWorld extends JavaPlugin {
    @Override
    public void onEnable() {
        //Load Files
        Config.setup();
        Config.addDefaults();
        Config.get().options().copyDefaults(true);
        Config.save();
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();

        //Startup Message
        System.out.println(" ");
        System.out.println("            " + ChatColor.GREEN + "Resource World " + ChatColor.UNDERLINE + "v" + this.getDescription().getVersion());
        System.out.println(" ");
        System.out.println("                   " + ChatColor.WHITE + "Author: " + ChatColor.UNDERLINE + "Nik");
        System.out.println(" ");
        System.out.println("     " + ChatColor.GREEN + "Running on " + ChatColor.WHITE + getServer().getVersion());
        System.out.println(" ");

        //Load Commands
        getCommand("resource").setExecutor(new CommandManager(this));

        //Implement Events
        getServer().getPluginManager().registerEvents(new LeaveInWorld(this), this);
        getServer().getPluginManager().registerEvents(new MenuHandler(this), this);
        new Initializer(this).initialize();

        //Create World
        if (!new WorldUtils().worldExists()) {
            new ResetTeleport(this).resetTP();
            new WorldGenerator(ResourceWorld.getPlugin(ResourceWorld.class)).createWorld();
        } else {
            System.out.println(Messenger.message("world_found"));
        }

        //Start Interval
        if (Config.get().getBoolean("world.settings.automated_resets.enabled")) {
            System.out.println(Messenger.message("automated_resets_enabled"));
            int interval = Config.get().getInt("world.settings.automated_resets.interval") * 72000;
            BukkitTask resetWorld = new ResetWorld(this).runTaskTimer(this, interval, interval);
        }

        //Check for updates
        if (Config.get().getBoolean("settings.check_for_updates")) {
            BukkitTask updateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            System.out.println(Messenger.message("update_disabled"));
        }

        //Enable bStats
        final int pluginId = 6981;
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
    }
}