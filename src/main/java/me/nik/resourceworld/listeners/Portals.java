package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;

public class Portals extends Manager {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalNether(PlayerPortalEvent e) {
        if (!configBoolean("nether_world.settings.override_portals")) return;
        if (!configBoolean("nether_world.settings.enabled")) return;
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();
        if (from.getEnvironment() == World.Environment.NETHER && to.getEnvironment() == World.Environment.NORMAL) {
            e.setTo(new Location(to, e.getFrom().getX(), e.getTo().getY(), e.getFrom().getZ()));
        } else if (from.getEnvironment() == World.Environment.NORMAL && to.getEnvironment() == World.Environment.NETHER) {
            e.setTo(new Location(Bukkit.getWorld(configString("nether_world.settings.world_name")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalEnd(PlayerPortalEvent e) {
        if (!configBoolean("end_world.settings.override_portals")) return;
        if (!configBoolean("end_world.settings.enabled")) return;
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();
        if (from.getEnvironment() == World.Environment.NORMAL && to.getEnvironment() == World.Environment.THE_END) {
            e.setTo(new Location(Bukkit.getWorld(configString("end_world.settings.world_name")), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
        }
    }
}
