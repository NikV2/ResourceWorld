package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGeneratorEnd {
    World world;

    private final String worldName = Config.Setting.END_NAME.getString();
    private final WorldType type = WorldType.valueOf(Config.Setting.END_TYPE.getString());
    private final World.Environment environment = World.Environment.valueOf(Config.Setting.END_ENVIRONMENT.getString());
    private final boolean useBorder = Config.Setting.END_BORDER_ENABLED.getBoolean();
    private final int border = Config.Setting.END_BORDER_SIZE.getInt();
    private final boolean pvp = Config.Setting.END_PVP.getBoolean();
    private final Difficulty difficulty = Difficulty.valueOf(Config.Setting.END_DIFFICULTY.getString());
    private final int monsterLimit = Config.Setting.END_MAX_MONSTERS.getInt();
    private final boolean loadSpawn = Config.Setting.END_KEEP_SPAWN_LOADED.getBoolean();
    private final boolean keepInventory = Config.Setting.END_KEEP_INVENTORY.getBoolean();

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(type);
            wc.environment(environment);
            world = wc.createWorld();
            final World resourceEnd = Bukkit.getWorld(worldName);
            if (useBorder) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(border);
            }
            resourceEnd.setPVP(pvp);
            resourceEnd.setDifficulty(difficulty);
            resourceEnd.setMonsterSpawnLimit(monsterLimit);
            resourceEnd.setKeepSpawnInMemory(loadSpawn);
            Bukkit.getWorlds().add(resourceEnd);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (keepInventory) {
                resourceEnd.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception e) {
            Messenger.consoleMessage("There was an error attempting to Generate a new World, Please contact the Author with the Following Error");
            e.printStackTrace();
        }
    }
}