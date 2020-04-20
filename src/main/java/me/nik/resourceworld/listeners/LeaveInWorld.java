package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld extends Manager {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (isInWorld(p)) {
            Location loc = Bukkit.getWorld(configString("settings.main_spawn_world")).getSpawnLocation();
            e.getPlayer().teleport(loc);
        }
    }
}