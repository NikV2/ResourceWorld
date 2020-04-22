package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGeneratorNether extends Manager {
    World world;

    public void createWorld() {
        try {
            WorldCreator wc = new WorldCreator(configString("nether_world.settings.world_name"));
            wc.type(WorldType.NORMAL);
            wc.environment(World.Environment.NETHER);
            world = wc.createWorld();
            final World resourceNether = Bukkit.getWorld(configString("nether_world.settings.world_name"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (configBoolean("nether_world.settings.world_border.enabled")) {
                        WorldBorder wb = Bukkit.getWorld(configString("nether_world.settings.world_name")).getWorldBorder();
                        wb.setCenter(0, 0);
                        wb.setSize(configInt("nether_world.settings.world_border.size"));
                    }
                    resourceNether.setPVP(configBoolean("nether_world.settings.allow_pvp"));
                    resourceNether.setDifficulty(Difficulty.valueOf(configString("nether_world.settings.difficulty")));
                    resourceNether.setAnimalSpawnLimit(configInt("nether_world.settings.entities.max_animals"));
                    resourceNether.setMonsterSpawnLimit(configInt("nether_world.settings.entities.max_monsters"));
                    resourceNether.setAmbientSpawnLimit(configInt("nether_world.settings.entities.max_ambient.entities"));
                }
            }.runTaskLaterAsynchronously(plugin, 30);
            resourceNether.setKeepSpawnInMemory(configBoolean("nether_world.settings.keep_spawn_loaded"));
            if (configBoolean("nether_world.settings.keep_inventory_on_death")) {
                resourceNether.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
            Bukkit.getWorlds().add(resourceNether);
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cSomething went wrong while generating your world, Please try restarting your Server and resetting your config.yml!")));
        }
    }
}
