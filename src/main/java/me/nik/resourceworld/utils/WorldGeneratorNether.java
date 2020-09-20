package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGeneratorNether {

    World world;

    private final String worldName = Config.Setting.NETHER_NAME.getString();
    private final WorldType type = WorldType.valueOf(Config.Setting.NETHER_TYPE.getString());
    private final World.Environment environment = World.Environment.valueOf(Config.Setting.NETHER_ENVIRONMENT.getString());
    private final boolean useBorder = Config.Setting.NETHER_BORDER_ENABLED.getBoolean();
    private final int border = Config.Setting.NETHER_BORDER_SIZE.getInt();
    private final boolean pvp = Config.Setting.NETHER_PVP.getBoolean();
    private final Difficulty difficulty = Difficulty.valueOf(Config.Setting.NETHER_DIFFICULTY.getString());
    private final int monsterLimit = Config.Setting.NETHER_MAX_MONSTERS.getInt();
    private final boolean loadSpawn = Config.Setting.NETHER_KEEP_SPAWN_LOADED.getBoolean();
    private final boolean keepInventory = Config.Setting.NETHER_KEEP_INVENTORY.getBoolean();

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(type);
            wc.environment(environment);
            world = wc.createWorld();
            final World resourceNether = Bukkit.getWorld(worldName);
            if (useBorder) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(border);
            }
            resourceNether.setPVP(pvp);
            resourceNether.setDifficulty(difficulty);
            resourceNether.setMonsterSpawnLimit(monsterLimit);
            resourceNether.setKeepSpawnInMemory(loadSpawn);
            Bukkit.getWorlds().add(resourceNether);
            if (MiscUtils.isLegacy()) return;
            if (keepInventory) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception e) {
            Messenger.consoleMessage("There was an error attempting to Generate a new World, Please contact the Author with the Following Error");
            e.printStackTrace();
        }
    }
}