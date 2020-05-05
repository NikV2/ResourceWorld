package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGenerator {
    World world;

    private final String worldName = Config.get().getString("world.settings.world_name");


    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(WorldType.valueOf(Config.get().getString("world.settings.world_type")));
            wc.generateStructures(Config.get().getBoolean("world.settings.generate_structures"));
            wc.environment(World.Environment.valueOf(Config.get().getString("world.settings.environment")));
            if (Config.get().getBoolean("world.settings.custom_seed.enabled")) {
                wc.seed(Config.get().getInt("world.settings.custom_seed.seed"));
            }
            world = wc.createWorld();
            final World resourceWorld = Bukkit.getWorld(worldName);
            if (Config.get().getBoolean("world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(Config.get().getInt("world.settings.world_border.size"));
            }
            resourceWorld.setPVP(Config.get().getBoolean("world.settings.allow_pvp"));
            resourceWorld.setDifficulty(Difficulty.valueOf(Config.get().getString("world.settings.difficulty")));
            resourceWorld.setAnimalSpawnLimit(Config.get().getInt("world.settings.entities.max_animals"));
            resourceWorld.setMonsterSpawnLimit(Config.get().getInt("world.settings.entities.max_monsters"));
            resourceWorld.setAmbientSpawnLimit(Config.get().getInt("world.settings.entities.max_ambient.entities"));
            resourceWorld.setStorm(Config.get().getBoolean("world.settings.weather_storms"));
            resourceWorld.setKeepSpawnInMemory(Config.get().getBoolean("world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceWorld);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (Config.get().getBoolean("world.settings.keep_inventory_on_death")) {
                resourceWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}