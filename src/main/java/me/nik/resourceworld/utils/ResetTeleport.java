package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResetTeleport {

    private final String world;
    private final String nether;
    private final String end;
    private final String spawn;

    public ResetTeleport(ResourceWorld plugin) {
        this.world = plugin.getConfig().getString("world.settings.world_name");
        this.nether = plugin.getConfig().getString("nether_world.settings.world_name");
        this.end = plugin.getConfig().getString("end_world.settings.world_name");
        this.spawn = plugin.getConfig().getString("settings.main_spawn_world");
    }

    public void resetTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(world)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message(MsgType.TELEPORTED_MESSAGE));
            }
        }
    }

    public void resetNetherTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(nether)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message(MsgType.TELEPORTED_MESSAGE));
            }
        }
    }

    public void resetEndTP() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(end)) {
                Location loc = Bukkit.getWorld(spawn).getSpawnLocation();
                p.teleport(loc);
                p.sendMessage(Messenger.message(MsgType.TELEPORTED_MESSAGE));
            }
        }
    }
}
