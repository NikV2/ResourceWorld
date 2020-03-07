package me.nik.resourceworld;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public final class ResourceWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        //Load Built in config
        Lang.setup();
        Lang.addDefaults();
        Lang.get().options().copyDefaults(true);
        Lang.save();
        Config.setup();
        Config.addDefaults();
        Config.get().options().copyDefaults(true);
        Config.save();
    }
}
