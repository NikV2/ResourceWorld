package me.nik.resourceworld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.WorldDeleter;
import me.nik.resourceworld.utils.WorldGenerator;
import org.bukkit.plugin.java.JavaPlugin;

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
        if (!getConfig().getBoolean("Enabled")) {
            System.out.println(ColourUtils.format(Lang.get().getString("Not Enabled")));
        } else {
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
