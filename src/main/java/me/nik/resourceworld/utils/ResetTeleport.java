package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResetTeleport {

    private final String world = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");
    private final String spawn = Config.get().getString("settings.main_spawn_world");

    public void resetTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(world)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }

    public void resetNetherTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(nether)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }

    public void resetEndTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(end)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }
}
