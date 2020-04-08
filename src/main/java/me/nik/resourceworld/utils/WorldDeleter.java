package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldDeleter extends Manager {

    public WorldDeleter(ResourceWorld plugin) {
        super(plugin);
    }

    public void deleteWorld() {
        if (!worldExists()) return;
        System.out.println(Messenger.message("deleting"));
        try {
            World world = Bukkit.getWorld(configString("world.settings.world_name"));
            Bukkit.unloadWorld(world, false);
            Bukkit.getWorlds().remove(world);
            deleteDirectory(world.getWorldFolder());
        } catch (NullPointerException ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cThere was an error while attempting to delete your previous Resource World, Please delete it manually or Reset your config.yml!")));
        }
    }
}
