package me.nik.resourceworld.utils;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGenerator {
    World world;
    ResourceWorld plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void createWorld() {
        System.out.println(Messenger.message("generating"));
        WorldCreator wc = new WorldCreator(Config.get().getString("world.settings.world_name"));
        wc.type(WorldType.valueOf(Config.get().getString("world.settings.world_type")));
        wc.generateStructures(Config.get().getBoolean("world.settings.generate_structures"));
        wc.environment(World.Environment.valueOf(Config.get().getString("world.settings.environment")));
        if (Config.get().getBoolean("world.settings.custom_seed.enabled")) {
            wc.seed(Config.get().getInt("world.settings.custom_seed.seed"));
        }
        world = wc.createWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Config.get().getBoolean("world.settings.world_border.enabled")) {
                    WorldBorder wb = Bukkit.getWorld(Config.get().getString("world.settings.world_name")).getWorldBorder();
                    wb.setCenter(0, 0);
                    wb.setSize(Config.get().getInt("world.settings.world_border.size"));
                }
                Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setPVP(Config.get().getBoolean("world.settings.allow_pvp"));
                Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setDifficulty(Difficulty.valueOf(Config.get().getString("world.settings.difficulty")));
                Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setAnimalSpawnLimit(Config.get().getInt("world.settings.entities.max_animals"));
                Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setMonsterSpawnLimit(Config.get().getInt("world.settings.entities.max_monsters"));
                Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setAmbientSpawnLimit(Config.get().getInt("world.settings.entities.max_ambient.entities"));
                cancel();
            }
        }.runTaskLaterAsynchronously(plugin, 30);
        Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setStorm(Config.get().getBoolean("world.settings.weather_storms"));
        Bukkit.getWorld(Config.get().getString("world.settings.world_name")).setKeepSpawnInMemory(Config.get().getBoolean("world.settings.keep_spawn_loaded"));
        System.gc();
        System.out.println(Messenger.message("generated"));
    }
}