package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LeaveInWorld implements Listener {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld().getName().equals(plugin.getConfig().getString("world_name"))) {
            Location loc = Bukkit.getWorld(plugin.getConfig().getString("fallback_world")).getSpawnLocation();
            p.teleport(loc);
        }
    }
}