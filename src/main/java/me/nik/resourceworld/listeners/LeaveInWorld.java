package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld extends Manager {

    public LeaveInWorld(ResourceWorld plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) return;
        Location loc = Bukkit.getWorld(configString("world.settings.main_spawn_world")).getSpawnLocation();
        e.getPlayer().teleport(loc);
    }
}