package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGenerator extends Manager {
    World world;

    public WorldGenerator(ResourceWorld plugin) {
        super(plugin);
    }

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(configString("world.settings.world_name"));
            wc.type(WorldType.valueOf(configString("world.settings.world_type")));
            wc.generateStructures(configBoolean("world.settings.generate_structures"));
            wc.environment(World.Environment.valueOf(configString("world.settings.environment")));
            if (configBoolean("world.settings.custom_seed.enabled")) {
                wc.seed(configInt("world.settings.custom_seed.seed"));
            }
            world = wc.createWorld();
            final World resourceWorld = Bukkit.getWorld(configString("world.settings.world_name"));
            if (configBoolean("world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(resourceWorld.getName()).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(configInt("world.settings.world_border.size"));
            }
            resourceWorld.setPVP(configBoolean("world.settings.allow_pvp"));
            resourceWorld.setDifficulty(Difficulty.valueOf(configString("world.settings.difficulty")));
            resourceWorld.setAnimalSpawnLimit(configInt("world.settings.entities.max_animals"));
            resourceWorld.setMonsterSpawnLimit(configInt("world.settings.entities.max_monsters"));
            resourceWorld.setAmbientSpawnLimit(configInt("world.settings.entities.max_ambient.entities"));
            resourceWorld.setStorm(configBoolean("world.settings.weather_storms"));
            resourceWorld.setKeepSpawnInMemory(configBoolean("world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceWorld);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (configBoolean("world.settings.keep_inventory_on_death")) {
                resourceWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}