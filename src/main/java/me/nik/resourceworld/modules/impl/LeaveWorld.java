package me.nik.resourceworld.modules.impl;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveWorld extends ListenerModule {

    private static final String LAST_RESOURCE_QUIT_WORLD_PATH = "quit_redirect.last_resource_world";

    public LeaveWorld(ResourceWorld plugin) {
        super(true, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        boolean inResourceWorld = this.plugin.getResourceWorlds()
                .values()
                .stream()
                .anyMatch(rw -> rw.getName().equals(player.getWorld().getName()));

        String path = LAST_RESOURCE_QUIT_WORLD_PATH + "." + player.getUniqueId();

        this.plugin.getData().set(path, inResourceWorld ? player.getWorld().getName() : null);
        this.plugin.saveData();

        if (!inResourceWorld) {
            return;
        }

        if (!Config.Setting.SETTINGS_TELEPORT_TO_SPAWN.getBoolean()) {
            return;
        }

        World spawnWorld = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString());

        if (spawnWorld != null) {
            player.teleport(spawnWorld.getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        String path = LAST_RESOURCE_QUIT_WORLD_PATH + "." + player.getUniqueId();
        String lastResourceWorldName = this.plugin.getData().getString(path);

        if (lastResourceWorldName == null || lastResourceWorldName.isEmpty()) {
            return;
        }

        if (player.getWorld().getName().equals(lastResourceWorldName)) {
            this.plugin.getData().set(path, null);
            this.plugin.saveData();
            return;
        }

        World spawnWorld = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString());

        if (spawnWorld == null) {
            return;
        }

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            player.teleport(spawnWorld.getSpawnLocation());
            this.plugin.getData().set(path, null);
            this.plugin.saveData();
        });
    }
}