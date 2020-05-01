package me.nik.resourceworld.api;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.List;

public class Manager implements Listener {

    protected final ResourceWorld plugin;

    public Manager(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    protected boolean configBoolean(String booleans) {
        return Config.get().getBoolean(booleans);
    }

    protected int configInt(String ints) {
        return Config.get().getInt(ints);
    }

    protected String configString(String string) {
        return Config.get().getString(string);
    }

    protected List<String> configStringList(String stringList) {
        return Config.get().getStringList(stringList);
    }

    protected void booleanSet(String path, boolean value) {
        Config.get().set(path, value);
    }

    protected void saveAndReload() {
        Config.save();
        Config.reload();
    }

    protected boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
        }
        return directory.delete();
    }

    protected boolean worldExists() {
        return Bukkit.getWorld(configString("world.settings.world_name")) != null;
    }

    protected boolean netherExists() {
        return Bukkit.getWorld(configString("nether_world.settings.world_name")) != null;
    }

    protected boolean endExists() {
        return Bukkit.getWorld(configString("end_world.settings.world_name")) != null;
    }

    protected boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(configString("nether_world.settings.world_name"))) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(configString("end_world.settings.world_name"));
    }
}
