package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveInWorld implements Listener {

    private final String world = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");
    private final String spawn = Config.get().getString("settings.main_spawn_world");

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