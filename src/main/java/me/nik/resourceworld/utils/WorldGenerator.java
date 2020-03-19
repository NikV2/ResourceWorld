package me.nik.resourceworld.utils;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGenerator {
    World world;
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void createWorld() {
        System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("generating")));
        WorldCreator wc = new WorldCreator(plugin.getConfig().getString("world_name"));
        wc.type(WorldType.valueOf(plugin.getConfig().getString("world_type")));
        wc.generateStructures(plugin.getConfig().getBoolean("generate_structures"));
        wc.environment(World.Environment.valueOf(plugin.getConfig().getString("environment")));
        if (plugin.getConfig().getBoolean("use_custom_seed")) {
            wc.seed(plugin.getConfig().getInt("seed"));
        }
        world = wc.createWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("world_border")) {
                    WorldBorder wb = Bukkit.getWorld(plugin.getConfig().getString("world_name")).getWorldBorder();
                    wb.setCenter(0, 0);
                    wb.setSize(plugin.getConfig().getInt("size"));
                }
                Bukkit.getWorld(plugin.getConfig().getString("world_name")).setPVP(plugin.getConfig().getBoolean("pvp"));
                Bukkit.getWorld(plugin.getConfig().getString("world_name")).setDifficulty(Difficulty.valueOf(plugin.getConfig().getString("difficulty")));
                Bukkit.getWorld(plugin.getConfig().getString("world_name")).setAnimalSpawnLimit(plugin.getConfig().getInt("max_animals"));
                Bukkit.getWorld(plugin.getConfig().getString("world_name")).setMonsterSpawnLimit(plugin.getConfig().getInt("max_monsters"));
                Bukkit.getWorld(plugin.getConfig().getString("world_name")).setAmbientSpawnLimit(plugin.getConfig().getInt("max_ambient_entities"));
            }
        }.runTaskAsynchronously(ResourceWorld.getPlugin(ResourceWorld.class));
        Bukkit.getWorld(plugin.getConfig().getString("world_name")).setStorm(plugin.getConfig().getBoolean("weather_storms"));
        Bukkit.getWorld(plugin.getConfig().getString("world_name")).setKeepSpawnInMemory(plugin.getConfig().getBoolean("keep_spawn_loaded"));
        System.gc();
        System.out.println(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("generated")));
    }
}