package me.nik.resourceworld.api;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.List;

public class Manager implements Listener {

    protected ResourceWorld plugin = ResourceWorld.getInstance();

    public boolean configBoolean(String booleans) {
        return Config.get().getBoolean(booleans);
    }

    public int configInt(String ints) {
        return Config.get().getInt(ints);
    }

    public String configString(String string) {
        return Config.get().getString(string);
    }

    public List<String> configStringList(String stringList) {
        return Config.get().getStringList(stringList);
    }

    public void booleanSet(String path, boolean value) {
        Config.get().set(path, value);
    }

    public void saveAndReload() {
        Config.save();
        Config.reload();
    }

    public boolean deleteDirectory(File directory) {
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

    public boolean worldExists() {
        return Bukkit.getWorld(configString("world.settings.world_name")) != null;
    }

    public boolean netherExists() {
        return Bukkit.getWorld(configString("nether_world.settings.world_name")) != null;
    }

    public boolean endExists() {
        return Bukkit.getWorld(configString("end_world.settings.world_name")) != null;
    }

    public boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(configString("nether_world.settings.world_name"))) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(configString("end_world.settings.world_name"));
    }
}
