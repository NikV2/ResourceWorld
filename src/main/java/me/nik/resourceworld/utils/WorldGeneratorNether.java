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

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.NETHER);
            world = wc.createWorld();
            final World resourceNether = Bukkit.getWorld(worldName);
            if (Config.get().getBoolean("nether_world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(Config.get().getInt("nether_world.settings.world_border.size"));
            }
            resourceNether.setPVP(Config.get().getBoolean("nether_world.settings.allow_pvp"));
            resourceNether.setDifficulty(Difficulty.valueOf(Config.get().getString("nether_world.settings.difficulty")));
            resourceNether.setMonsterSpawnLimit(Config.get().getInt("nether_world.settings.entities.max_monsters"));
            resourceNether.setKeepSpawnInMemory(Config.get().getBoolean("nether_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceNether);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (Config.get().getBoolean("world.settings.keep_inventory_on_death")) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}
