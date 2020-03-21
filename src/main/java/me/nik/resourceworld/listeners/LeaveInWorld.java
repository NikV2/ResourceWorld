package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld().getName().equals(Config.get().getString("world.settings.world_name"))) {
            Location loc = Bukkit.getWorld(Config.get().getString("world.settings.main_spawn_world")).getSpawnLocation();
            p.teleport(loc);
        }
    }
}