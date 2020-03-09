package me.nik.resourceworld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.tasks.ResetWorld;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.WorldDeleter;
import me.nik.resourceworld.utils.WorldGenerator;
import org.bukkit.Bukkit;
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
        System.out.println("§r                                   ");
        System.out.println("§r            §a§lResource World §o§nv1.0.0");
        System.out.println("§r                                   ");
        System.out.println("§r                   §f§lAuthor: §nNik");
        System.out.println("§r                                   ");
        System.out.println("§r     §a§lRunning on §b§n§l" + ServerVersion);
        System.out.println("§r                                   ");

        //Create World
        //Start Interval
        if (!getConfig().getBoolean("Enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Not Enabled")));
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        } else if (!getConfig().getBoolean("Automated Resets")) {
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Automated Resets Disabled")));
            new WorldGenerator().createWorld();
         }else{
            System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Automated Resets Enabled")));
            int interval = getConfig().getInt("Interval") * 72000;
            BukkitTask ResetWorld = new ResetWorld(this).runTaskTimer(this, interval, interval);
            new WorldGenerator().createWorld();
        }
    }
    @Override
    public void onDisable() {
        //Delete World
        if (getConfig().getBoolean("Enabled")) {
            new WorldDeleter().deleteWorld();
        }
    }
}
