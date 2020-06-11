package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Portals implements Listener {

    private final ResourceWorld plugin;

    private final String world;
    private final String nether;
    private final String end;

    public Portals(ResourceWorld plugin) {
        this.plugin = plugin;
        this.world = plugin.getConfig().getString("world.settings.world_name");
        this.nether = plugin.getConfig().getString("nether_world.settings.world_name");
        this.end = plugin.getConfig().getString("end_world.settings.world_name");
    }

    @EventHandler
    public void onPortalNether(PlayerPortalEvent e) {
        if (!plugin.getConfig().getBoolean("nether_world.settings.enabled")) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL || e.getFrom().getWorld().getName().equalsIgnoreCase(world)) {
            if (plugin.getConfig().getBoolean("nether_world.settings.portals.vanilla_portal_ratio")) {
                e.setTo(new Location(Bukkit.getWorld(nether), e.getFrom().getX() % 8, e.getFrom().getY() % 8, e.getFrom().getZ() % 8));
            } else {
                e.setTo(new Location(Bukkit.getWorld(nether), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER || e.getFrom().getWorld().getName().equalsIgnoreCase(nether)) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(plugin.getConfig().getString("nether_world.settings.portals.portal_world")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        }
    }

    @EventHandler
    public void onPortalEnd(PlayerPortalEvent e) {
        if (!plugin.getConfig().getBoolean("end_world.settings.enabled")) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END)) {
            e.setTo(new Location(Bukkit.getWorld(end), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ()));
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END || e.getFrom().getWorld().getName().equalsIgnoreCase(end)) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(plugin.getConfig().getString("end_world.settings.portals.portal_world")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
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
