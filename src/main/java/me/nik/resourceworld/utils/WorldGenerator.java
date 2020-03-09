package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGenerator {
    World world;

    public void createWorld() {
        System.out.println(Lang.get().getString("Generating"));
        WorldCreator wc = new WorldCreator(Config.get().getString("World Name"));
        wc.type(WorldType.valueOf(Config.get().getString("World Type")));
        wc.generateStructures(Config.get().getBoolean("Generate Structures"));
        if (Config.get().getBoolean("Use Custom Seed")) {
            wc.seed(Config.get().getInt("Seed"));
        }
        world = wc.createWorld();
        Bukkit.getServer().getWorld(Config.get().getString("World Name")).save();
        System.out.println(Lang.get().getString("Generated"));
    }
}