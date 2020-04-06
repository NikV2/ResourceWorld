package me.nik.resourceworld.api;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
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

    public boolean isVersionSupported() {
        return Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16");
    }

    public void booleanSet(String path, boolean value) {
        Config.get().set(path, value);
    }

    public void saveAndReload() {
        Config.save();
        Config.reload();
    }
}
