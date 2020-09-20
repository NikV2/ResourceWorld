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

    private final String worldName = Config.Setting.WORLD_NAME.getString();
    private final WorldType type = WorldType.valueOf(Config.Setting.WORLD_TYPE.getString());
    private final boolean structures = Config.Setting.WORLD_GENERATE_STRUCTURES.getBoolean();
    private final World.Environment environment = World.Environment.valueOf(Config.Setting.WORLD_ENVIRONMENT.getString());
    private final boolean useSeed = Config.Setting.WORLD_SEED_ENABLED.getBoolean();
    private final long seed = Config.Setting.WORLD_SEED.getLong();
    private final boolean useBorder = Config.Setting.WORLD_BORDER_ENABLED.getBoolean();
    private final int border = Config.Setting.WORLD_BORDER_SIZE.getInt();
    private final boolean pvp = Config.Setting.WORLD_PVP.getBoolean();
    private final Difficulty difficulty = Difficulty.valueOf(Config.Setting.WORLD_DIFFICULTY.getString());
    private final int animalLimit = Config.Setting.WORLD_MAX_ANIMALS.getInt();
    private final int monsterLimit = Config.Setting.WORLD_MAX_MONSTERS.getInt();
    private final int ambientLimit = Config.Setting.WORLD_MAX_AMBIENT.getInt();
    private final boolean storms = Config.Setting.WORLD_WEATHER_STORMS.getBoolean();
    private final boolean loadSpawn = Config.Setting.WORLD_KEEP_SPAWN_LOADED.getBoolean();
    private final boolean keepInventory = Config.Setting.WORLD_KEEP_INVENTORY.getBoolean();

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
            if (MiscUtils.isLegacy()) return;
            if (keepInventory) {
                resourceWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception e) {
            Messenger.consoleMessage("There was an error attempting to Generate a new World, Please contact the Author with the Following Error");
            e.printStackTrace();
        }
    }
}