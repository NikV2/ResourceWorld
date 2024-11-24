package me.nik.resourceworld.modules.impl;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveWorld extends ListenerModule {

    public LeaveWorld(ResourceWorld plugin) {
        super(Config.Setting.SETTINGS_TELEPORT_TO_SPAWN.getBoolean(), plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        if (this.plugin.getResourceWorlds()
                .values()
                .stream()
                .anyMatch(rw -> rw.getName().equals(player.getWorld().getName()))) {
            player.teleport(Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation());
        }
    }
}