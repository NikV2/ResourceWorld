package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

public class WorldDeleter {
    public void deleteWorld() {
        String wname = Config.get().getString("WorldName");
        Bukkit.getServer().getWorld(wname).getWorldFolder().deleteOnExit();
        Bukkit.getServer().getWorld(wname).getWorldFolder().delete();
        new WorldGenerator().createWorld();

    }
}
