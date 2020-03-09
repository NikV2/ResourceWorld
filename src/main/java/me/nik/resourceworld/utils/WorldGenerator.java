package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;

public class WorldGenerator {
    World world;
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void createWorld() {
        System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Generating")));
        WorldCreator wc = new WorldCreator(plugin.getConfig().getString("World Name"));
        wc.type(WorldType.valueOf(plugin.getConfig().getString("World Type")));
        wc.generateStructures(plugin.getConfig().getBoolean("Generate Structures"));
        if (plugin.getConfig().getBoolean("Use Custom Seed")) {
            wc.seed(plugin.getConfig().getInt("Seed"));
        }
        world = wc.createWorld();
        Bukkit.getServer().getWorld(plugin.getConfig().getString("World Name")).save();
        System.out.println(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Generated")));
    }
}