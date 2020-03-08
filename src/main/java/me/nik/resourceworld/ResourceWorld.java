package me.nik.resourceworld;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.WorldChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class ResourceWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        //Load Built in configs
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();
        Config.setup();
        Config.addDefaults();
        Config.get().options().copyDefaults(true);
        Config.save();

        //Startup Message
        String ServerVersion = getServer().getVersion();
        System.out.println("§r                                   ");
        System.out.println("§r            §a§lResource World §2§o§nv1.0.0");
        System.out.println("§r                                   ");
        System.out.println("§r     §a§lRunning on §f§n§l" + ServerVersion);
        System.out.println("§r                                   ");

        //Check, Unload, Delete, Create
        if (!Config.get().getBoolean("Enabled")){
            System.out.println(Lang.get().getString("NotEnabled"));
        }else{
            new WorldChecker().wCheck();
        }
    }
}
