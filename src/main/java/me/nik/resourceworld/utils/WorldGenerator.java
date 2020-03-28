package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGenerator extends Manager {
    World world;

    public void createWorld() {
        System.out.println(Messenger.message("generating"));
        WorldCreator wc = new WorldCreator(configString("world.settings.world_name"));
        wc.type(WorldType.valueOf(configString("world.settings.world_type")));
        wc.generateStructures(configBoolean("world.settings.generate_structures"));
        wc.environment(World.Environment.valueOf(configString("world.settings.environment")));
        if (configBoolean("world.settings.custom_seed.enabled")) {
            wc.seed(configInt("world.settings.custom_seed.seed"));
        }
        world = wc.createWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (configBoolean("world.settings.world_border.enabled")) {
                    WorldBorder wb = Bukkit.getWorld(configString("world.settings.world_name")).getWorldBorder();
                    wb.setCenter(0, 0);
                    wb.setSize(configInt("world.settings.world_border.size"));
                }
                Bukkit.getWorld(configString("world.settings.world_name")).setPVP(configBoolean("world.settings.allow_pvp"));
                Bukkit.getWorld(configString("world.settings.world_name")).setDifficulty(Difficulty.valueOf(configString("world.settings.difficulty")));
                Bukkit.getWorld(configString("world.settings.world_name")).setAnimalSpawnLimit(configInt("world.settings.entities.max_animals"));
                Bukkit.getWorld(configString("world.settings.world_name")).setMonsterSpawnLimit(configInt("world.settings.entities.max_monsters"));
                Bukkit.getWorld(configString("world.settings.world_name")).setAmbientSpawnLimit(configInt("world.settings.entities.max_ambient.entities"));
            }
        }.runTaskLaterAsynchronously(plugin, 40);
        Bukkit.getWorld(configString("world.settings.world_name")).setStorm(configBoolean("world.settings.weather_storms"));
        Bukkit.getWorld(configString("world.settings.world_name")).setKeepSpawnInMemory(configBoolean("world.settings.keep_spawn_loaded"));
        System.out.println(Messenger.message("generated"));
    }
}