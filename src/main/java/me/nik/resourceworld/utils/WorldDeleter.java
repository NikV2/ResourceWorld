package me.nik.resourceworld.utils;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class WorldDeleter {
    public void deleteWorld() {
        System.out.println(Lang.get().getString("Deleting"));
        World world = Bukkit.getWorld(Config.get().getString("World Name"));
        Bukkit.unloadWorld(world,false);
        FileUtils.deleteQuietly(new File(world.getName()));
    }
}
