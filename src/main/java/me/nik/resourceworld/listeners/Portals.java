package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Portals extends Manager {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalNether(PlayerPortalEvent e) {
        if (!configBoolean("nether_world.settings.enabled")) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL || e.getFrom().getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) {
            if (configBoolean("nether_world.settings.portals.vanilla_portal_ratio")) {
                e.setTo(new Location(Bukkit.getWorld(configString("nether_world.settings.world_name")), e.getFrom().getX() % 8, e.getFrom().getY() % 8, e.getFrom().getZ() % 8));
            } else {
                e.setTo(new Location(Bukkit.getWorld(configString("nether_world.settings.world_name")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER || e.getFrom().getWorld().getName().equalsIgnoreCase(configString("nether_world.settings.world_name"))) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(configString("nether_world.settings.portals.portal_world")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalEnd(PlayerPortalEvent e) {
        if (!configBoolean("end_world.settings.enabled")) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END)) {
            e.setTo(new Location(Bukkit.getWorld(configString("end_world.settings.world_name")), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ()));
        } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END || e.getFrom().getWorld().getName().equalsIgnoreCase(configString("end_world.settings.world_name"))) {
            if (isSupported()) {
                e.setTo(e.getTo());
            } else {
                e.setTo(new Location(Bukkit.getWorld(configString("end_world.settings.portals.portal_world")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            }
        }
    }

    private boolean isSupported() {
        return !Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSuffocateNether(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!p.getWorld().getName().equalsIgnoreCase(configString("nether_world.settings.world_name"))) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
    }
}
