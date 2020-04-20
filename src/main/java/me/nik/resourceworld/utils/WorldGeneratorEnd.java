package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldGeneratorEnd extends Manager {
    World world;

    public void createWorld() {
        System.out.println(Messenger.message("generating"));
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
                    resourceEnd.setAnimalSpawnLimit(configInt("end_world.settings.entities.max_animals"));
                    resourceEnd.setMonsterSpawnLimit(configInt("end_world.settings.entities.max_monsters"));
                    resourceEnd.setAmbientSpawnLimit(configInt("end_world.settings.entities.max_ambient.entities"));
                }
            }.runTaskLaterAsynchronously(plugin, 30);
            resourceEnd.setKeepSpawnInMemory(configBoolean("end_world.settings.keep_spawn_loaded"));
            Bukkit.getWorlds().add(resourceEnd);
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException ignored) {
            System.out.println(Messenger.prefix(Messenger.format("&cSomething went wrong while generating your world, Please try restarting your Server and resetting your config.yml!")));
        }
    }
}
