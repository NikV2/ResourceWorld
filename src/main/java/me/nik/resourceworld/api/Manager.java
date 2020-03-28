package me.nik.resourceworld.api;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class Manager implements Listener {

    public Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);

    public boolean configBoolean(String booleans) {
        return Config.get().getBoolean(booleans);
    }

    public int configInt(String ints) {
        return Config.get().getInt(ints);
    }

    public double configDouble(String doubles) {
        return Config.get().getDouble(doubles);
    }

    public String configString(String string) {
        return Config.get().getString(string);
    }

    public List<String> configStringList(String stringList) {
        return Config.get().getStringList(stringList);
    }
}
