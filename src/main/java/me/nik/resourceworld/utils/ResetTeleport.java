package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResetTeleport {

    public void resetTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(Config.Setting.WORLD_NAME.getString())) {
                Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
            }
        }
    }

    public void resetNetherTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(Config.Setting.NETHER_NAME.getString())) {
                Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
            }
        }
    }

    public void resetEndTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(Config.Setting.END_NAME.getString())) {
                Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
            }
        }
    }
}