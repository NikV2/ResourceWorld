package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.custom.CustomWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld implements Listener {

    private final ResourceWorld plugin;

    public LeaveInWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent e) {

        final Player p = e.getPlayer();

        final String world = p.getWorld().getName();

        for (CustomWorld rw : this.plugin.getResourceWorlds().values()) {
            if (!rw.getName().equals(world)) continue;

            Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();

            if (loc != null) {
                p.teleport(loc);
            }
        }
    }
}