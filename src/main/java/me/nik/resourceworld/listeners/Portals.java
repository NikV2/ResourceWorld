package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Portals implements Listener {

    @EventHandler
    public void onPortalNether(PlayerPortalEvent e) {
        if (!Config.Setting.NETHER_ENABLED.getBoolean()) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL || e.getFrom().getWorld().getName().equalsIgnoreCase(Config.Setting.WORLD_NAME.getString())) {
            if (Config.Setting.NETHER_PORTALS_VANILLA_RATIO.getBoolean()) {
                e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()), e.getFrom().getX() % 8, e.getFrom().getY() % 8, e.getFrom().getZ() % 8));
            } else {
                e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER || e.getFrom().getWorld().getName().equalsIgnoreCase(Config.Setting.NETHER_NAME.getString())) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(Config.Setting.NETHER_PORTALS_PORTALWORLD.getString()), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        }
    }

    @EventHandler
    public void onPortalEnd(PlayerPortalEvent e) {
        if (!Config.Setting.END_ENABLED.getBoolean()) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END)) {
            e.setTo(new Location(Bukkit.getWorld(Config.Setting.END_NAME.getString()), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ()));
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END || e.getFrom().getWorld().getName().equalsIgnoreCase(Config.Setting.END_NAME.getString())) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(Config.Setting.END_PORTALS_PORTALWORLD.getString()), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        }
    }

    private boolean isSupported() {
        return !Bukkit.getVersion().contains("1.8")
                && !Bukkit.getVersion().contains("1.9")
                && !Bukkit.getVersion().contains("1.10")
                && !Bukkit.getVersion().contains("1.11")
                && !Bukkit.getVersion().contains("1.12")
                && !Bukkit.getVersion().contains("1.13");
    }
}