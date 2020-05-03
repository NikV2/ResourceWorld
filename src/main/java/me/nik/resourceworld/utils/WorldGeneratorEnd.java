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

public class WorldGeneratorEnd extends Manager {
    World world;

    public WorldGeneratorEnd(ResourceWorld plugin) {
        super(plugin);
    }

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(configString("end_world.settings.world_name"));
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.THE_END);
            world = wc.createWorld();
            final World resourceEnd = Bukkit.getWorld(configString("end_world.settings.world_name"));
            if (configBoolean("end_world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(configString("end_world.settings.world_name")).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(configInt("end_world.settings.world_border.size"));
            }
            resourceEnd.setPVP(configBoolean("end_world.settings.allow_pvp"));
            resourceEnd.setDifficulty(Difficulty.valueOf(configString("end_world.settings.difficulty")));
            resourceEnd.setMonsterSpawnLimit(configInt("end_world.settings.entities.max_monsters"));
            resourceEnd.setKeepSpawnInMemory(configBoolean("end_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceEnd);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (configBoolean("end_world.settings.keep_inventory_on_death")) {
                resourceEnd.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
        }
    }
}
