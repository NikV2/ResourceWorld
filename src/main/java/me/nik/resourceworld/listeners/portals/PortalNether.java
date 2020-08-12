package me.nik.resourceworld.listeners.portals;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalNether implements Listener {

    @EventHandler
    public void onPortalWorld(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        if (e.getFrom().getWorld().getEnvironment() != World.Environment.NORMAL) return;

        if (Config.Setting.NETHER_PORTALS_ONLY_RESOURCE.getBoolean() && !e.getFrom().getWorld().getName().equals(Config.Setting.WORLD_NAME.getString()))
            return;

        if (Config.Setting.NETHER_PORTALS_VANILLA_RATIO.getBoolean()) {
            e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()), e.getFrom().getX() % 8, e.getFrom().getY() % 8, e.getFrom().getZ() % 8));
        } else {
            e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
        }
    }

    @EventHandler
    public void onPortalNether(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        if (e.getFrom().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (!e.getFrom().getWorld().getName().equals(Config.Setting.NETHER_NAME.getString())) return;

        e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_PORTALS_PORTALWORLD.getString()), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
    }
}