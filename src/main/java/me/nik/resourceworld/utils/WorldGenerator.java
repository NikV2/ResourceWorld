package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGenerator {
    World world;

    private final String worldName;
    private final WorldType type;
    private final boolean structures;
    private final World.Environment environment;
    private final boolean useSeed;
    private final long seed;
    private final boolean useBorder;
    private final int border;
    private final boolean pvp;
    private final Difficulty difficulty;
    private final int animalLimit;
    private final int monsterLimit;
    private final int ambientLimit;
    private final boolean storms;
    private final boolean loadSpawn;
    private final boolean keepInventory;

    public WorldGenerator(ResourceWorld plugin) {
        this.worldName = plugin.getConfig().getString("world.settings.world_name");
        this.type = WorldType.valueOf(plugin.getConfig().getString("world.settings.world_type"));
        this.structures = plugin.getConfig().getBoolean("world.settings.generate_structures");
        this.environment = World.Environment.valueOf(plugin.getConfig().getString("world.settings.environment"));
        this.useSeed = plugin.getConfig().getBoolean("world.settings.custom_seed.enabled");
        this.seed = plugin.getConfig().getLong("world.settings.custom_seed.seed");
        this.useBorder = plugin.getConfig().getBoolean("world.settings.world_border.enabled");
        this.border = plugin.getConfig().getInt("world.settings.world_border.size");
        this.pvp = plugin.getConfig().getBoolean("world.settings.allow_pvp");
        this.difficulty = Difficulty.valueOf(plugin.getConfig().getString("world.settings.difficulty"));
        this.animalLimit = plugin.getConfig().getInt("world.settings.entities.max_animals");
        this.monsterLimit = plugin.getConfig().getInt("world.settings.entities.max_monsters");
        this.ambientLimit = plugin.getConfig().getInt("world.settings.entities.max_ambient_entities");
        this.storms = plugin.getConfig().getBoolean("world.settings.weather_storms");
        this.loadSpawn = plugin.getConfig().getBoolean("world.settings.keep_spawn_loaded");
        this.keepInventory = plugin.getConfig().getBoolean("world.settings.keep_inventory_on_death");
    }


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
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (keepInventory) {
                resourceWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception e) {
            Messenger.consoleMessage("There was an error attempting to Generate a new World, Please contact the Author with the Following Error");
            e.printStackTrace();
        }
    }
}