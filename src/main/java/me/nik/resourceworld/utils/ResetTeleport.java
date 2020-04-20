package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResetTeleport extends Manager {

    public void resetTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(configString("world.settings.world_name"))) {
                Location loc = Bukkit.getWorld(configString("settings.main_spawn_world")).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }

    public void resetNetherTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(configString("nether_world.settings.world_name"))) {
                Location loc = Bukkit.getWorld(configString("settings.main_spawn_world")).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }

    public void resetEndTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(configString("end_world.settings.world_name"))) {
                Location loc = Bukkit.getWorld(configString("settings.main_spawn_world")).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }
}
