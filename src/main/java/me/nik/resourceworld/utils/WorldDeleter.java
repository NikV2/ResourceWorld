package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class WorldDeleter extends Manager {

    public void deleteWorld() {
        System.out.println(Messenger.message("deleting"));
        try {
            World world = Bukkit.getWorld(configString("world.settings.world_name"));
            Bukkit.unloadWorld(world, false);
            FileUtils.deleteQuietly(new File(world.getName()));
        } catch (NullPointerException ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
        }
    }
}
