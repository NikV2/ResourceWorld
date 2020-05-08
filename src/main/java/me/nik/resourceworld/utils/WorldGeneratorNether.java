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

    private final String worldName = Config.get().getString("nether_world.settings.world_name");
    private final WorldType type = WorldType.valueOf(Config.get().getString("nether_world.settings.world_type"));
    private final World.Environment environment = World.Environment.valueOf(Config.get().getString("nether_world.settings.environment"));
    private final boolean useBorder = Config.get().getBoolean("nether_world.settings.world_border.enabled");
    private final int border = Config.get().getInt("nether_world.settings.world_border.size");
    private final boolean pvp = Config.get().getBoolean("nether_world.settings.allow_pvp");
    private final Difficulty difficulty = Difficulty.valueOf(Config.get().getString("nether_world.settings.difficulty"));
    private final int monsterLimit = Config.get().getInt("nether_world.settings.entities.max_monsters");
    private final boolean loadSpawn = Config.get().getBoolean("nether_world.settings.keep_spawn_loaded");
    private final boolean keepInventory = Config.get().getBoolean("world.settings.keep_inventory_on_death");

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
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (keepInventory) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}
