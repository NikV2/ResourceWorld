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
    private final WorldType type = WorldType.valueOf(Config.get().getString("world.settings.world_type"));
    private final boolean structures = Config.get().getBoolean("world.settings.generate_structures");
    private final World.Environment environment = World.Environment.valueOf(Config.get().getString("world.settings.environment"));
    private final boolean useSeed = Config.get().getBoolean("world.settings.custom_seed.enabled");
    private final int seed = Config.get().getInt("world.settings.custom_seed.seed");
    private final boolean useBorder = Config.get().getBoolean("world.settings.world_border.enabled");
    private final int border = Config.get().getInt("world.settings.world_border.size");
    private final boolean pvp = Config.get().getBoolean("world.settings.allow_pvp");
    private final Difficulty difficulty = Difficulty.valueOf(Config.get().getString("world.settings.difficulty"));
    private final int animalLimit = Config.get().getInt("world.settings.entities.max_animals");
    private final int monsterLimit = Config.get().getInt("world.settings.entities.max_monsters");
    private final int ambientLimit = Config.get().getInt("world.settings.entities.max_ambient_entities");
    private final boolean storms = Config.get().getBoolean("world.settings.weather_storms");
    private final boolean loadSpawn = Config.get().getBoolean("world.settings.keep_spawn_loaded");
    private final boolean keepInventory = Config.get().getBoolean("world.settings.keep_inventory_on_death");


    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(type);
            wc.generateStructures(structures);
            wc.environment(environment);
            if (useSeed) {
                wc.seed(seed);
            }
            world = wc.createWorld();
            final World resourceWorld = Bukkit.getWorld(worldName);
            if (useBorder) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(border);
            }
            resourceWorld.setPVP(pvp);
            resourceWorld.setDifficulty(difficulty);
            resourceWorld.setAnimalSpawnLimit(animalLimit);
            resourceWorld.setMonsterSpawnLimit(monsterLimit);
            resourceWorld.setAmbientSpawnLimit(ambientLimit);
            resourceWorld.setStorm(storms);
            resourceWorld.setKeepSpawnInMemory(loadSpawn);
            Bukkit.getWorlds().add(resourceWorld);
            new WorldCommands().worldRunCommands();
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (keepInventory) {
                resourceWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}