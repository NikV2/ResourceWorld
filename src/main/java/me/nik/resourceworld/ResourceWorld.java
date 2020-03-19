package me.nik.resourceworld;

import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.listeners.DisabledCmds;
import me.nik.resourceworld.listeners.LeaveInWorld;
import me.nik.resourceworld.listeners.MenuHandler;
import me.nik.resourceworld.tasks.ResetWorld;
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
        //Load Built in Files
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

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
        if (!getConfig().getBoolean("enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("not_enabled")));
        } else if (!getConfig().getBoolean("automated_resets")) {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("automated_resets_disabled")));
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
        } else {
            System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("automated_resets_enabled")));
            int interval = getConfig().getInt("interval") * 72000;
            BukkitTask ResetWorld = new ResetWorld(this).runTaskTimer(this, interval, interval);
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
        }
    }

    @Override
    public void onDisable() {
        //Delete World
        if (getConfig().getBoolean("enabled")) {
            new ResetTeleport().resetTP();
            new WorldDeleter().deleteWorld();
        }
        reloadConfig();
        saveConfig();
    }
}