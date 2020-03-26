package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class WorldDeleter {
    public void deleteWorld() {
        System.out.println(Messenger.message("deleting"));
        World world = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
        Bukkit.unloadWorld(world, false);
        FileUtils.deleteQuietly(new File(world.getName()));
    }
}
