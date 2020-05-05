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

    private final String worldName = Config.get().getString("end_world.settings.world_name");

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(worldName);
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.THE_END);
            world = wc.createWorld();
            final World resourceEnd = Bukkit.getWorld(worldName);
            if (Config.get().getBoolean("end_world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(worldName).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(Config.get().getInt("end_world.settings.world_border.size"));
            }
            resourceEnd.setPVP(Config.get().getBoolean("end_world.settings.allow_pvp"));
            resourceEnd.setDifficulty(Difficulty.valueOf(Config.get().getString("end_world.settings.difficulty")));
            resourceEnd.setMonsterSpawnLimit(Config.get().getInt("end_world.settings.entities.max_monsters"));
            resourceEnd.setKeepSpawnInMemory(Config.get().getBoolean("end_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceEnd);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (Config.get().getBoolean("end_world.settings.keep_inventory_on_death")) {
                resourceEnd.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}
