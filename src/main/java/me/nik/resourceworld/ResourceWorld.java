package me.nik.resourceworld;

import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.DisabledCmds;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.MenuHandler;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldDeleter;
import me.nik.resourceworld.utils.WorldGenerator;
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
        getCommand("Resource").setExecutor(new CommandManager());

        //Implement Events
        getServer().getPluginManager().registerEvents(new LeaveInWorld(), this);
        getServer().getPluginManager().registerEvents(new MenuHandler(), this);
        getServer().getPluginManager().registerEvents(new DisabledCmds(), this);

        //Create World
        //Start Interval
        if (!Config.get().getBoolean("settings.enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("not_enabled")));
        } else if (!Config.get().getBoolean("world.settings.automated_resets.enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("automated_resets_disabled")));
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
        } else {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("automated_resets_enabled")));
            int interval = Config.get().getInt("world.settings.automated_resets.interval") * 72000;
            BukkitTask ResetWorld = new ResetWorld(this).runTaskTimer(this, interval, interval);
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
        }
        //Check for updates
        if (Config.get().getBoolean("settings.check_for_updates")) {
            BukkitTask UpdateChecker = new UpdateChecker(this).runTaskAsynchronously(this);
        } else {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("update_disabled")));
        }
    }

    @Override
    public void onDisable() {
        //Delete World
        if (Config.get().getBoolean("settings.enabled")) {
            new ResetTeleport().resetTP();
            new WorldDeleter().deleteWorld();
        }
        //Reload Files
        Config.reload();
        Config.save();
        Lang.reload();
        Lang.save();
    }
}