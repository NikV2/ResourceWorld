package me.nik.resourceworld.utils;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;

public class WorldGenerator {
    World world;
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void createWorld() {
        System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("generating")));
        WorldCreator wc = new WorldCreator(plugin.getConfig().getString("world_name"));
        wc.type(WorldType.valueOf(plugin.getConfig().getString("world_type")));
        wc.generateStructures(plugin.getConfig().getBoolean("generate_structures"));
        if (plugin.getConfig().getBoolean("use_custom_seed")) {
            wc.seed(plugin.getConfig().getInt("seed"));
        }
        world = wc.createWorld();
        if (plugin.getConfig().getBoolean("world_border")) {
            WorldBorder wb = Bukkit.getWorld(plugin.getConfig().getString("world_name")).getWorldBorder();
            wb.setCenter(0, 0);
            wb.setSize(plugin.getConfig().getInt("size"));
        }
        System.gc();
        System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("generated")));
    }
}