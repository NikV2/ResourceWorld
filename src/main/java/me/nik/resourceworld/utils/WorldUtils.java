package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

public class WorldUtils {
    public boolean worldExists() {
        return Bukkit.getWorld(Config.get().getString("world.settings.world_name")) != null;
    }
}
