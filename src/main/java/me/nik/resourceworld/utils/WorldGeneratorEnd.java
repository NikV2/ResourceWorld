package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGeneratorEnd extends Manager {
    World world;

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(configString("end_world.settings.world_name"));
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.THE_END);
            world = wc.createWorld();
            final World resourceEnd = Bukkit.getWorld(configString("end_world.settings.world_name"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (configBoolean("end_world.settings.world_border.enabled")) {
                        WorldBorder wb = Bukkit.getWorld(configString("end_world.settings.world_name")).getWorldBorder();
                        wb.setCenter(0, 0);
                        wb.setSize(configInt("end_world.settings.world_border.size"));
                    }
                    resourceEnd.setPVP(configBoolean("end_world.settings.allow_pvp"));
                    resourceEnd.setDifficulty(Difficulty.valueOf(configString("end_world.settings.difficulty")));
                    resourceEnd.setMonsterSpawnLimit(configInt("end_world.settings.entities.max_monsters"));
                }
            }.runTaskLaterAsynchronously(plugin, 30);
            resourceEnd.setKeepSpawnInMemory(configBoolean("end_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceEnd);
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
                return;
            if (configBoolean("end_world.settings.keep_inventory_on_death")) {
                resourceEnd.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        } catch (Exception ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cSomething went wrong while generating your world, Please try restarting your Server and resetting your config.yml!")));
        }
    }
}
