package me.nik.resourceworld;
import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.files.Lang;
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
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Startup Message
        String ServerVersion = getServer().getVersion();
        System.out.println("                                   ");
        System.out.println("            " + ChatColor.GREEN + "Resource World " + ChatColor.UNDERLINE + "v1.0.0");
        System.out.println("                                   ");
        System.out.println("                   " + ChatColor.WHITE + "Author: " + ChatColor.UNDERLINE + "Nik");
        System.out.println("                                   ");
        System.out.println("     " + ChatColor.GREEN + "Running on " + ChatColor.WHITE + ServerVersion);
        System.out.println("                                   ");

        //Load Commands
        getCommand("Resource").setExecutor(new CommandManager());

        //Create World
        //Start Interval
        if (!getConfig().getBoolean("Enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Not Enabled")));
            getServer().getPluginManager().disablePlugin(this);
        } else if (!getConfig().getBoolean("Automated Resets")) {
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Automated Resets Disabled")));
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
         }else{
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Automated Resets Enabled")));
            int interval = getConfig().getInt("Interval") * 72000;
            BukkitTask ResetWorld = new ResetWorld(this).runTaskTimer(this, interval, interval);
            new ResetTeleport().resetTP();
            new WorldGenerator().createWorld();
        }
    }
    @Override
    public void onDisable() {
        //Delete World
        if (getConfig().getBoolean("Enabled")) {
            new ResetTeleport().resetTP();
            new WorldDeleter().deleteWorld();
        }
    }
}
