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

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (isInWorld(p)) {
            Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();
            p.teleport(loc);
        }
    }

    private boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(Config.Setting.WORLD_NAME.getString())) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(Config.Setting.NETHER_NAME.getString())) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(Config.Setting.END_NAME.getString());
    }
}