package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld implements Listener {

    private final String world;
    private final String nether;
    private final String end;
    private final String spawn;

    public LeaveInWorld(ResourceWorld plugin) {
        this.world = plugin.getConfig().getString("world.settings.world_name");
        this.nether = plugin.getConfig().getString("nether_world.settings.world_name");
        this.end = plugin.getConfig().getString("end_world.settings.world_name");
        this.spawn = plugin.getConfig().getString("settings.main_spawn_world");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (isInWorld(p)) {
            Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
            e.getPlayer().teleport(loc);
        }
    }

    private boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(world)) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(nether)) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(end);
    }
}