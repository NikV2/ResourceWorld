package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGeneratorNether extends Manager {
    World world;

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(configString("nether_world.settings.world_name"));
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.NETHER);
            world = wc.createWorld();
            final World resourceNether = Bukkit.getWorld(configString("nether_world.settings.world_name"));
            if (configBoolean("nether_world.settings.world_border.enabled")) {
                WorldBorder wb = Bukkit.getWorld(configString("nether_world.settings.world_name")).getWorldBorder();
                wb.setCenter(0, 0);
                wb.setSize(configInt("nether_world.settings.world_border.size"));
            }
            resourceNether.setPVP(configBoolean("nether_world.settings.allow_pvp"));
            resourceNether.setDifficulty(Difficulty.valueOf(configString("nether_world.settings.difficulty")));
            resourceNether.setMonsterSpawnLimit(configInt("nether_world.settings.entities.max_monsters"));
            resourceNether.setKeepSpawnInMemory(configBoolean("nether_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceNether);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (configBoolean("world.settings.keep_inventory_on_death")) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cSomething went wrong while generating your world, Please try restarting your Server and resetting your config.yml!")));
        }
    }
}
