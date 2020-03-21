package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResetTeleport {
    public void resetTP() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(Config.get().getString("world.settings.world_name"))) {
                Location loc = Bukkit.getWorld(Config.get().getString("world.settings.main_spawn_world")).getSpawnLocation();
                p.teleport(loc);
            }
        }
    }
}
