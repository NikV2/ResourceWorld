package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WorldDeleter {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void deleteWorld() {
        System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("deleting")));
        World world = Bukkit.getWorld(plugin.getConfig().getString("world_name"));
        Bukkit.unloadWorld(world, false);
        FileUtils.deleteQuietly(new File(world.getName()));
    }
}
